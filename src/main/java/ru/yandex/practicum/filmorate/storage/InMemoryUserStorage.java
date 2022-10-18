package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Long id = 1L;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Optional<User> update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Update: " + user + "to:" + user);
            return Optional.of(user);
        } else {
            log.error("Поступил запрос на редактирование пользователя, которого нет.");
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> delete(User user) {
        return Optional.of(users.remove(user.getId()));
    }

    @Override
    public User create(User user) {
        user.setId(id);
        users.put(id, user);
        log.info("Add:" + user);
        id++;
        return user;
    }

    @Override
    public ArrayList<User> get() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUserById(Long id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        } else {
            return Optional.empty();
        }
    }
}
