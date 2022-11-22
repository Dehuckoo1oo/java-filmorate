package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmStorageTest {
    private final FilmDbStorage filmStorage;

    /*@Test
    public void testFindFilmById() {
        Optional<User> userOptional = filmStorage.getUserById(1L);
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
    public void testGet() {
        List<User> userOptional = userStorage.get();
        assertThat(userOptional).asList().size().isEqualTo(3);
    }*/
}
