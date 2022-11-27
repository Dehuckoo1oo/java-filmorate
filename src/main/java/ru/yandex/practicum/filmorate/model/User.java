package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
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
    private final Set<Long> friends = new HashSet<>();

    public void addFriend(Long id) {
        friends.add(id);
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

    public void setFriends(Set<Long> newFriends) {
        friends.clear();
        friends.addAll(newFriends);
    }
}
