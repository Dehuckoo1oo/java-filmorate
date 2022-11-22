package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.MPAMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public interface FilmStorage {
    public Optional<Film> update(Film film);

    public Optional<Film> delete(Film film);

    public Film create(Film film);

    public List<Film> get();

    public Optional<Film> getFilmById(Long id);

    public Optional<Genre> getGenreById(int id);

    public Optional<MPA> getMPAById(int id);

    public List<Genre> getGenre();

    public List<MPA> getMPA();

}
