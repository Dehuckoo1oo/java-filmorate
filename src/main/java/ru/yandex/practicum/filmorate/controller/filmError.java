package ru.yandex.practicum.filmorate.controller;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class filmError {
    List<String> errors;

    public filmError(List<String> errors) {
        this.errors = errors;
    }
}
