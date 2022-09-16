package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Validated
public class Film {

    private Long id;
    @NotBlank(message = "Необходимо указать название фильма")
    private String name;
    @Size(message = "Описание превышает 200 символов", max = 200)
    private String description;
    private LocalDate releaseDate;
    @PositiveOrZero(message = "Длительность не может быть меньше 0")
    private int duration;

}
