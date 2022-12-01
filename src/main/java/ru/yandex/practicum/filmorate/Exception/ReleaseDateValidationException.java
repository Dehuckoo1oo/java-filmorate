package ru.yandex.practicum.filmorate.Exception;

public class ReleaseDateValidationException extends RuntimeException {

    public ReleaseDateValidationException() {
    }

    public ReleaseDateValidationException(String message) {
        super(message);
    }

    public ReleaseDateValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReleaseDateValidationException(Throwable cause) {
        super(cause);
    }
}
