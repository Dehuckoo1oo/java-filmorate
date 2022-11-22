package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public interface UserStorage {
    public User update(User user);

    public User delete(User user);

    public User create(User user);

    public List<User> get();

    public Optional<User> getUserById(Long id);
}
