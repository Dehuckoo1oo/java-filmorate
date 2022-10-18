package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.Exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@Slf4j
public class FilmController {

    FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @DeleteMapping("/films")
    public ResponseEntity<Film> delete(Film film) {
        Optional<Film> entity = filmService.delete(film);
        return entity.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/films/{id}/like/{userId}")
    public ResponseEntity<Film> likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        try {
            return ResponseEntity.ok(filmService.addLike(id,userId));
        } catch (UserNotFoundException | FilmNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public ResponseEntity<Film> removeLikeFromFilm(@PathVariable Long id, @PathVariable Long userId) {
        try {
            return ResponseEntity.ok(filmService.removeLike(id,userId));
        } catch (UserNotFoundException | FilmNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") Long count) {
            return filmService.getTopFilms(count);
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        return filmService.get();
    }

    @GetMapping("/films/{filmId}")
    public ResponseEntity<Film> findFilm(@PathVariable Long filmId) {
        Optional<Film> entity = filmService.getFilmById(filmId);
        return entity.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/films")
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        return ResponseEntity.ok(filmService.create(film));
    }

    @PutMapping("/films")
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        Long idNewFilm = film.getId();
        if (idNewFilm == null) {
            return ResponseEntity.ok(filmService.create(film));
        } else {
            return filmService.update(film)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleException(MethodArgumentNotValidException e) {
        log.info(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<String> handleException(ValidationException e) {
        log.info(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
