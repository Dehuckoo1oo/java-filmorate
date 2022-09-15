package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@Slf4j
public class FilmController {
    int id = 1;
    Map<Integer, Film> films = new HashMap<>();
    @GetMapping("/films")
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null) {
            log.info("Add:" + film);
            checkRelease(film);
            film.setId(id);
            films.put(id, film);
            id++;
            return ResponseEntity.ok(film);
        } else {
            return new ResponseEntity<Film>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/films")
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        Integer idNewFilm = film.getId();
        if (idNewFilm == null) {
            log.info("Add:" + film);
            checkRelease(film);
            film.setId(id);
            films.put(id, film);
            id++;
            return ResponseEntity.ok(film);
        } else if (films.containsKey(idNewFilm)){
            checkRelease(film);
            Film oldFilm = films.get(idNewFilm);
            log.info("Update:" + oldFilm + " to:" + film);
            oldFilm.setDescription(film.getDescription());
            oldFilm.setName(film.getName());
            oldFilm.setReleaseDate(film.getReleaseDate());
            oldFilm.setDuration(film.getDuration());
            return ResponseEntity.ok(film);
        } else {
            log.error("Поступил запрос на редактирование фильма, которого нет");
            return new ResponseEntity<Film>(HttpStatus.NOT_FOUND);
        }
    }

    private void checkRelease(Film film){
        if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))){
            throw new ValidationException("Дата выхода фильма некорректна");
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
