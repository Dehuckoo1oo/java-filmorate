package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

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
    private Long id = 1L;
    private Map<Long, Film> films = new HashMap<>();
    @GetMapping("/films")
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
            checkRelease(film);
            film.setId(id);
            films.put(id, film);
            log.info("Add:" + film);
            id++;
            return ResponseEntity.ok(film);
    }

    @PutMapping("/films")
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        Long idNewFilm = film.getId();
        if (idNewFilm == null) {
            checkRelease(film);
            film.setId(id);
            films.put(id, film);
            log.info("Add:" + film);
            id++;
            return ResponseEntity.ok(film);
        } else if (films.containsKey(idNewFilm)){
            checkRelease(film);
            films.put(film.getId(),film);
            log.info("Update:" + film + " to:" + film);
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
