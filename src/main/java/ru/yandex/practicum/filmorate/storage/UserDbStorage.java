package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Repository
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO PUBLIC.USERS (EMAIL, LOGIN, NAME, BIRTHDAY)" +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        Set<Long> friends = user.getFriends();
        if (!friends.isEmpty()) {
            String sqlQueryFriends = "INSERT INTO PUBLIC.FRIEND_LIST " +
                    "(USER_ID, FRIEND_ID)" +
                    "VALUES(?, ?);";
            for (Long friend : friends) {
                jdbcTemplate.update(sqlQueryFriends, user.getId(), friend);
            }
        }
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE PUBLIC.USERS " +
                "SET EMAIL=?, LOGIN=?, NAME=?, BIRTHDAY=?" +
                "WHERE ID=?;";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(),
                Date.valueOf(user.getBirthday()), user.getId());
        String sqlQueryFriends = "INSERT INTO PUBLIC.FRIEND_LIST" +
                "(USER_ID, FRIEND_ID)" +
                "VALUES(?, ?);";
        String sqlQueryDelFriends = "DELETE FROM PUBLIC.FRIEND_LIST WHERE USER_ID=?";
        Set<Long> friends = user.getFriends();
        if (!friends.isEmpty()) {
            jdbcTemplate.update(sqlQueryDelFriends, user.getId());
            for (Long friend : friends) {
                jdbcTemplate.update(sqlQueryFriends, user.getId(), friend);
            }
        } else {
            jdbcTemplate.update(sqlQueryDelFriends, user.getId());
        }
        return user;
    }

    @Override
    public User delete(User user) {
        String sqlQuery = "DELETE FROM PUBLIC.USERS WHERE ID=?";
        jdbcTemplate.update(sqlQuery, user.getId());
        return user;
    }


    /*@Override
    public List<User> get() {
        // метод принимает в виде аргумента строку запроса, преобразователь и аргумент — id пользователя
        String sql = "SELECT * FROM PUBLIC.USERS";
        List<User> users = jdbcTemplate.query(sql, new UserMapper());
        users.forEach(this::fillFriends);
        return users;
    }*/


    @Override
    public List<User> get() {
        String sql = "SELECT * FROM PUBLIC.USERS";
        List<User> users = jdbcTemplate.query(sql, new UserMapper());
        String sqlFriend = "SELECT user_id,friend_id FROM PUBLIC.friend_list";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlFriend);
        Map<Long, Set<Long>> friends = new HashMap<>();
        while (rs.next()) {
            Long userId = rs.getLong("user_id");
            Long friendId = rs.getLong("friend_id");
            if (friends.containsKey(userId)) {
                friends.get(userId).add(friendId);
            } else {
                Set<Long> friendEntity = new HashSet<>();
                friendEntity.add(friendId);
                friends.put(userId, friendEntity);
            }
        }
        users.forEach(user -> user.setFriends(friends.getOrDefault(user.getId(), new HashSet<>())));
        return users;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        String sql = "SELECT * FROM PUBLIC.USERS WHERE id=?";
        Optional<User> user = jdbcTemplate.query(sql, new UserMapper(), id).stream().findAny();
        user.ifPresent(this::fillFriends);
        return user;
    }

    private void fillFriends(User user) {
        String sql = "SELECT friend_id FROM PUBLIC.friend_list WHERE user_id=?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, user.getId());
        while (rs.next()) {
            user.addFriend(rs.getLong("friend_id"));
        }
    }
}
