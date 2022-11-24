package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.constraints.Positive;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriends(Long id1, Long id2) {
        User user = checkUser(id1);
        User user2 = checkUser(id2);
        user.addFriend(user2.getId());
        userStorage.update(user);
        return user;
    }

    public User deleteFriends(Long id1, Long id2) {
        User usr1 = checkUser(id1);
        User usr2 = checkUser(id2);
        usr1.deleteFriend(usr2.getId());
        usr2.deleteFriend(usr1.getId());
        userStorage.update(usr1);
        userStorage.update(usr2);
        return usr1;
    }

    public List<User> mutualFriends(Long id1, Long id2) {
        User usr1 = checkUser(id1);
        User usr2 = checkUser(id2);
        List<User> matualFriends = new ArrayList<>();
        usr1.getFriends().stream()
                .filter(usr2.getFriends()::contains)
                .forEach(value -> matualFriends.add(getUserById(value)));
        return matualFriends;
    }

    public List<User> getFriendList(Long userId) {
        User user = checkUser(userId);
        List<User> friendsList = new ArrayList<>();
        user.getFriends().forEach(value -> friendsList.add(getUserById(value)));
        return friendsList;
    }


    public Optional<User> update(User user) {
        checkName(user);
        checkUser(user.getId());
        log.info("Update: " + user + "to:" + user);
        return Optional.ofNullable(userStorage.update(user));
    }

    public Optional<User> delete(User user) {
        return Optional.ofNullable(userStorage.delete(user));
    }

    public User create(User user) {
        checkName(user);
        log.info("Add:" + user);
        return userStorage.create(user);
    }

    public List<User> get() {
        return userStorage.get();
    }

    public User getUserById(Long id) {
        return checkUser(id);
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private User checkUser(Long id) {
        Optional<User> usr = userStorage.getUserById(id);
        if (usr.isPresent()) {
            return usr.get();
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }
}
