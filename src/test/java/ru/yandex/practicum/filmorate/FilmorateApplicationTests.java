package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userStorage.getUserById(1L);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "denis")
                );
    }

    @Test
    public void testCreateUser() {
        User userPavel = new User(null, "pavel@mail.ru", "pavel",
                "pavel", LocalDate.of(2099, 1, 1));
        userStorage.create(userPavel);
        Optional<User> userOptional = userStorage.getUserById(4L);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "pavel")
                );
    }

    @Test
    public void testUpdateUser() {
        User userPavel = new User(3L, "Arcadiy@gmail.com", "ArcadiyDeathKnight",
                "denis", LocalDate.of(2022, 1, 1));
        userStorage.update(userPavel);
        Optional<User> userOptional = userStorage.getUserById(3L);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "ArcadiyDeathKnight")
                );
    }

    @Test
    public void testDeleteUser() {
        User userPavel = new User(1L, "denis@gmail.com", "denis",
                "denis", LocalDate.of(2022, 1, 1));
        userStorage.delete(userPavel);
        Optional<User> userOptional = userStorage.getUserById(1L);
        assertThat(userOptional)
                .isEmpty();
    }

    @Test
    public void testGetUsers() {
        List<User> userOptional = userStorage.get();
        assertThat(userOptional).asList().size().isEqualTo(3);
    }

    @Test
    public void testFindFilmById() {
        Optional<Film> filmOptional = filmStorage.getFilmById(1L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Avatar")
                );
    }

    @Test
    public void testCreateFilm() {
        Film filmTheLionKing = new Film(null, "The Lion King", "fantasy",
                LocalDate.of(1895, 12, 27), 90, new ArrayList<>(), new MPA(1, "test"));
        filmStorage.create(filmTheLionKing);
        Optional<Film> filmOptional = filmStorage.getFilmById(4L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "The Lion King")
                );
    }

    @Test
    public void testUpdateFilm() {
        Film filmOnceInHollywood = new Film(3L, "Once in Hollywood", "action movie",
                LocalDate.of(1895, 12, 27), 90, new ArrayList<>(), new MPA(1, "test"));
        filmStorage.update(filmOnceInHollywood);
        Optional<Film> filmOptional = filmStorage.getFilmById(3L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Once in Hollywood")
                );
    }

    @Test
    public void testDeleteFilm() {
        Film filmTest = new Film(2L, null, null,
                LocalDate.of(1895, 12, 27), 90, new ArrayList<>(), new MPA(0, "test"));
        filmStorage.delete(filmTest);
        Optional<Film> filmOptional = filmStorage.getFilmById(2L);
        assertThat(filmOptional)
                .isEmpty();
    }

    @Test
    public void testGetFilms() {
        List<Film> filmOptional = filmStorage.get();
        assertThat(filmOptional).asList().size().isEqualTo(3);
    }

}
