package ru.yandex.practicum.filmorate.controller;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class filmError {
    String errors;

    public filmError(String errors) {
        this.errors = errors;
    }
}
