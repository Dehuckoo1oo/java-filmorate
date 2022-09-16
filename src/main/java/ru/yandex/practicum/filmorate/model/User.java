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
public class User {

    private Long id;
    @NotBlank(message = "Необходимо указать Email")
    @Email(message = "Email указан не корректно")
    private String email;
    @NotBlank(message = "Необходимо указать Login")
    private String login;
    private String name;
    @Past(message = "Некорректная дата рождения")
    private LocalDate birthday;
}
