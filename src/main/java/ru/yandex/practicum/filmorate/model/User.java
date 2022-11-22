package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
    private final Map<Long,Boolean> friends = new HashMap<>();

    public void addFriend(Long id,Boolean isConfirmed) {
        if(friends.getOrDefault(id,false)){
            friends.put(id,true);
        } else {
            friends.put(id,isConfirmed);
        }
    }

    public void deleteFriend(Long id) {
        friends.remove(id);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("EMAIL", email);
        values.put("LOGIN", login);
        values.put("NAME", name);
        values.put("BIRTHDAY", birthday);
        return values;
    }
}
