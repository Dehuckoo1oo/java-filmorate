package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class userError {
    String error;

    public userError(String error) {
        this.error = error;
    }
}
