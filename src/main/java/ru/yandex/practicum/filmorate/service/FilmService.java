package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import ru.yandex.practicum.filmorate.Exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.Exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@ApplicationScope
public class FilmService {

    FilmStorage filmStorage;
    UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Optional<Film> update(Film film) {
        checkRelease(film);
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

    public Film addLike(Long filmId, Long userId) throws FilmNotFoundException, UserNotFoundException {
        Film film = checkFilm(filmId);
        User user = userService.getUserById(userId);
        film.like(user.getId());
        return film;
    }

    public Film removeLike(Long filmId, Long userId) throws FilmNotFoundException, UserNotFoundException {
        Film film = checkFilm(filmId);
        User user = userService.getUserById(userId);
        film.removeLike(user.getId());
        return film;
    }

    public List<Film> getTopFilms(Long count) {
        List<Film> topFilms = new ArrayList<>();
        filmStorage.get().stream().sorted(comparator).limit(count).forEach(topFilms::add);
        return topFilms;
    }

    public Optional<Film> getFilmById(Long id){
        return filmStorage.getFilmById(id);
    }

    private void checkRelease(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата выхода фильма некорректна");
        }
    }

    private Film checkFilm(Long filmId) throws FilmNotFoundException {
        Optional<Film> film = filmStorage.getFilmById(filmId);
        if(film.isPresent()){
            return film.get();
        } else {
            throw new FilmNotFoundException("Данный фильм не найден");
        }
    }

    Comparator<Film> comparator = new Comparator<Film>(){
        @Override
        public int compare(Film o1, Film o2) {
            return o2.countLikes()  - o1.countLikes();
        }
    };

}
