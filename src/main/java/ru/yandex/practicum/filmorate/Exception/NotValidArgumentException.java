package ru.yandex.practicum.filmorate.Exception;

import java.io.IOException;

public class NotValidArgumentException extends IOException {
    public NotValidArgumentException() {
    }

    public NotValidArgumentException(String message) {
        super(message);
    }

    public NotValidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotValidArgumentException(Throwable cause) {
        super(cause);
    }
}
