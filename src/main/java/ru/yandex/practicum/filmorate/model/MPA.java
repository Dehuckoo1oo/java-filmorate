package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class MPA {
    @NotNull
    Integer id;
    String name;
}
