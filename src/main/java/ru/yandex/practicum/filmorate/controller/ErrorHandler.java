package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.Exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.Exception.ReleaseDateValidationException;
import ru.yandex.practicum.filmorate.Exception.UserNotFoundException;

import javax.validation.ValidationException;


@Slf4j
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({FilmNotFoundException.class})
    public ResponseEntity<Object> handleException(FilmNotFoundException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(new userError(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException e) {
        log.info(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<Object> handleException(ValidationException e) {
        log.info(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        System.out.println(name + " parameter is missing");
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> handleException(UserNotFoundException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(new userError(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ReleaseDateValidationException.class})
    public ResponseEntity<Object> handleException(ReleaseDateValidationException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(new filmError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
