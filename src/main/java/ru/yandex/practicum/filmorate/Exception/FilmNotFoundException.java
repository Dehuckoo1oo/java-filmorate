package ru.yandex.practicum.filmorate.Exception;

import java.io.IOException;

public class FilmNotFoundException extends IOException {
    public FilmNotFoundException() {
    }

    public FilmNotFoundException(String message) {
        super(message);
    }

    public FilmNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilmNotFoundException(Throwable cause) {
        super(cause);
    }
}
