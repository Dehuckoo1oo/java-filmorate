package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exception.NotValidArgumentException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Optional;

@Component
public interface UserStorage {
    public Optional<User> update(User user);

    public Optional<User> delete(User user);

    public User create(User user);

    public ArrayList<User> get();

    public Optional<User> getUserById(Long id);
}
