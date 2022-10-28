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
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User delete(User user) {
        return users.remove(user.getId());
    }

    @Override
    public User create(User user) {
        user.setId(id);
        users.put(id, user);
        id++;
        return user;
    }

    @Override
    public ArrayList<User> get() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

}
