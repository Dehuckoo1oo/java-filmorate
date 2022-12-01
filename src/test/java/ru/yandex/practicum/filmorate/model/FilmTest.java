package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.Exception.ReleaseDateValidationException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmTest {
    @Test
    void checkValidationErrors() {
        Film film = new Film(null, null, "fantasyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy",
                LocalDate.of(1895, 12, 27), -1, new ArrayList<>(), new MPA(0, "test"));

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        Map<String, String> tests = new HashMap<>();
        tests.put("name", "Необходимо указать название фильма");
        tests.put("description", "Описание превышает 200 символов");
        tests.put("duration", "Длительность не может быть меньше 0");
        for (Map.Entry<String, String> test : tests.entrySet()) {
            ConstraintViolation<Film> violation = violations.stream()
                    .filter(path -> path.getPropertyPath().toString().equals(test.getKey()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Отсутствует ошибка валидации"));
            assertEquals(test.getKey(), violation.getPropertyPath().toString());
            assertEquals(test.getValue(), violation.getMessageTemplate());
        }
        FilmController filmcontroller = new FilmController(
                new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));

        final ReleaseDateValidationException exception = assertThrows(
                ReleaseDateValidationException.class,
                () -> filmcontroller.addFilm(film));
        assertEquals("releaseDate =1895-12-27", exception.getMessage());
    }
}