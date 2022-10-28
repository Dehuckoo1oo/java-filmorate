package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

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
        User usr1 = checkUser(id1);
        User usr2 = checkUser(id2);
        usr1.addFriend(usr2.getId(),true);
        usr2.addFriend(usr1.getId(),false);
        return usr1;
    }

    public User deleteFriends(Long id1, Long id2) {
        User usr1 = checkUser(id1);
        User usr2 = checkUser(id2);
        usr1.deleteFriend(usr2.getId());
        usr2.deleteFriend(usr1.getId());
        return usr1;
    }

    public List<User> mutualFriends(Long id1, Long id2) {
        User usr1 = checkUser(id1);
        User usr2 = checkUser(id2);
        ArrayList<Long> friendList1 = new ArrayList<>();
        ArrayList<Long> friendList2 = new ArrayList<>();
        usr1.getFriends().entrySet().stream()
                .filter(Map.Entry::getValue)
                .forEach(entity -> friendList1.add(entity.getKey()));
        usr2.getFriends().entrySet().stream()
                .filter(Map.Entry::getValue)
                .forEach(entity -> friendList2.add(entity.getKey()));
        List<User> matualFriends = new ArrayList<>();
        friendList1.stream()
                .filter(friendList2::contains)
                .forEach(value -> matualFriends.add(getUserById(value)));
        return matualFriends;
    }

    public List<User> getFriendList(Long userId) {
        User user = checkUser(userId);
        List<User> friendsList = new ArrayList<>();
        user.getFriends().entrySet().stream()
                .filter(Map.Entry::getValue)
                .forEach(value -> friendsList.add(getUserById(value.getKey())));
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

    public ArrayList<User> get() {
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
