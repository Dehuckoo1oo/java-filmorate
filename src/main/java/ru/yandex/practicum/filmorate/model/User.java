package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor()
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
    private final Set<Long> friends = new HashSet<>();

    public void addFriend(Long id){
        friends.add(id);
    }

    public void deleteFriend(Long id){
        friends.remove(id);
    }

    public int countFriends(){
        return friends.size();
    }
}
