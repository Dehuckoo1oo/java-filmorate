package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Genre {
    int id;
    String name;
}
