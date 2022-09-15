package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Validated
public class Film{
    private static final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);

    private Integer id;
    @NotBlank(message = "Необходимо указать название фильма")
    private String name;
    @Size(message = "Описание превышает 200 символов",max = 200)
    private String description;
    private LocalDate releaseDate;
    @PositiveOrZero(message = "Длительность не может быть меньше 0")
    private int duration;

}
