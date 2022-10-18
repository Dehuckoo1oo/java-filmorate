package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriends(Long id1, Long id2) throws UserNotFoundException {
        User usr1 = checkUser(id1);
        User usr2 = checkUser(id2);
        usr1.addFriend(usr2.getId());
        usr2.addFriend(usr1.getId());
        return usr1;
    }

    public User deleteFriends(Long id1, Long id2) throws UserNotFoundException {
        User usr1 = checkUser(id1);
        User usr2 = checkUser(id2);
        usr1.deleteFriend(usr2.getId());
        usr2.deleteFriend(usr1.getId());
        return usr1;
    }

    public List<User> mutualFriends(Long id1, Long id2) throws UserNotFoundException {
        User usr1 = checkUser(id1);
        User usr2 = checkUser(id2);
        Set<Long> friendList1 = usr1.getFriends();
        Set<Long> friendList2 = usr2.getFriends();
        List<User> matualFriends = new ArrayList<>();
        if (friendList1 != null && friendList2 != null) {
            for (Long i : friendList1) {
                if (friendList2.contains(i)) {
                    matualFriends.add(getUserById(i));
                }
            }
        }
        return matualFriends;
    }

    public List<User> getFriendList(Long userId) throws UserNotFoundException {
        User user = checkUser(userId);
        List<User> friendsList = new ArrayList<>();
        for (Long usrID : user.getFriends()) {
            friendsList.add(getUserById(usrID));
        }
        return friendsList;
    }


    public Optional<User> update(User user) {
        checkName(user);
        return userStorage.update(user);
    }

    public Optional<User> delete(User user) {
        return userStorage.delete(user);
    }

    public User create(User user) {
        checkName(user);
        return userStorage.create(user);
    }

    public ArrayList<User> get() {
        return userStorage.get();
    }

    public User getUserById(Long id) throws UserNotFoundException {
        return checkUser(id);
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private User checkUser(Long id) throws UserNotFoundException {
        Optional<User> usr = userStorage.getUserById(id);
        if (usr.isPresent()) {
            return usr.get();
        } else {
            throw new UserNotFoundException("Пользователь не найден");

        }
    }

}
