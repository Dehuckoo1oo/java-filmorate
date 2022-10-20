package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import ru.yandex.practicum.filmorate.Exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.Exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@ApplicationScope
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final Comparator<Film> comparator = new Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            return o2.countLikes() - o1.countLikes();
        }
    };

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Optional<Film> update(Film film) {
        checkRelease(film);
        checkFilm(film.getId());
        log.info("Update:" + film + " to:" + film);
        return filmStorage.update(film);
    }

    public Optional<Film> delete(Film film) {
        return filmStorage.delete(film);
    }

    public Film create(Film film) {
        checkRelease(film);
        return filmStorage.create(film);
    }

    public List<Film> get() {
        return filmStorage.get();
    }

    public Film addLike(Long filmId, Long userId) {
        Film film = checkFilm(filmId);
        Optional<User> user = userStorage.getUserById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException("Пользователь не найден");
        }
        film.like(user.get().getId());
        filmStorage.update(film);
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        Film film = checkFilm(filmId);
        Optional<User> user = userStorage.getUserById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException("Пользователь не найден");
        }
        film.removeLike(user.get().getId());
        filmStorage.update(film);
        return film;
    }

    public List<Film> getTopFilms(Long count) {
        List<Film> topFilms = new ArrayList<>();
        filmStorage.get().stream().sorted(comparator).limit(count).forEach(topFilms::add);
        return topFilms;
    }

    public Optional<Film> getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    private void checkRelease(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата выхода фильма некорректна");
        }
    }

    private Film checkFilm(Long filmId) {
        Optional<Film> film = filmStorage.getFilmById(filmId);
        if (film.isPresent()) {
            return film.get();
        } else {
            throw new FilmNotFoundException("Данный фильм не найден");
        }
    }
}
