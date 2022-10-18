package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Component
public interface FilmStorage {
    public Optional<Film> update(Film film);

    public Optional<Film> delete(Film film);

    public Film create(Film film);

    public List<Film> get();

    public Optional<Film> getFilmById(Long id);
}
