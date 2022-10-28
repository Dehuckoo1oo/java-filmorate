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
    private final Set<String> genre = new HashSet<>();
    private final Set<String> MPA = new HashSet<>();
    private final Set<Long> likes = new HashSet<>();

    public Film like(Long id) {
        likes.add(id);
        return this;
    }

    public Film removeLike(Long id) {
        likes.remove(id);
        return this;
    }

    public int countLikes() {
        return likes.size();
    }

    public void addGenre(String filmGenre){
        genre.add(filmGenre);
    }

    public void addMPA(String MPARating){
        MPA.add(MPARating);
    }
}


