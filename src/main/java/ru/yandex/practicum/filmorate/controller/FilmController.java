package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@Slf4j
public class FilmController {

    private final FilmService filmService;

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
        return ResponseEntity.ok(filmService.addLike(id, userId));
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public ResponseEntity<Film> removeLikeFromFilm(@PathVariable Long id, @PathVariable Long userId) {
        return ResponseEntity.ok(filmService.removeLike(id, userId));
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
        return ResponseEntity.ok(filmService.getFilmById(filmId));
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

    @GetMapping("/genres/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable int id) {
        Optional<Genre> entity = filmService.getGenreById(id);
        return entity.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/mpa/{id}")
    public ResponseEntity<MPA> getMPAById(@PathVariable int id) {
        Optional<MPA> entity = filmService.getMPAById(id);
        return entity.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/genres")
    public List<Genre> getGenre() {
        return filmService.getGenre();
    }

    @GetMapping("/mpa")
    public List<MPA> getMPA() {
        return filmService.getMPA();
    }
}
