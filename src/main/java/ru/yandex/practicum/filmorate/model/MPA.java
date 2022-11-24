package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

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
