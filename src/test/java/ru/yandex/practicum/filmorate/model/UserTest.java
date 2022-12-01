package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {
    @Test
    void checkValidationErrors() {

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        User user = new User(null, null, null,
                null, LocalDate.of(2099, 1, 1));

        Set<ConstraintViolation<User>> violationsUser = validator.validate(user);

        Map<String, String> tests = new HashMap<>();
        tests.put("email", "Необходимо указать Email");
        tests.put("login", "Необходимо указать Login");
        tests.put("birthday", "Некорректная дата рождения");
        for (Map.Entry<String, String> test : tests.entrySet()) {
            ConstraintViolation<User> violationUser = violationsUser.stream()
                    .filter(path -> path.getPropertyPath().toString().equals(test.getKey()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Отсутствует ошибка валидации"));
            assertEquals(test.getKey(), violationUser.getPropertyPath().toString());
            assertEquals(test.getValue(), violationUser.getMessageTemplate());
        }

        user = new User(null, "Denis", "Denis",
                null, LocalDate.of(1997, 2, 26));

        Set<ConstraintViolation<User>> violationsUserSecond = validator.validate(user);
        ConstraintViolation<User> violationUserSecond = violationsUser.stream()
                .filter(path -> path.getPropertyPath().toString().equals("email"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Отсутствует ошибка валидации"));
        assertEquals("email", violationUserSecond.getPropertyPath().toString());
        assertEquals("Необходимо указать Email", violationUserSecond.getMessageTemplate());
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
        userController.addUser(user);
        assertEquals(user.getName(), "Denis");
    }

}