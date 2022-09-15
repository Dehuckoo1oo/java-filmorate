package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@Slf4j
public class UserController {
    Integer id = 1;
    Map<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        checkName(user);
        if (user.getId() == null) {
            log.info("Add:" + user);
            user.setId(id);
            users.put(id, user);
            id++;
            return ResponseEntity.ok(user);
        } else {
            log.error("Поступил запрос на создание пользователя с id");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        Integer idNewUser = user.getId();
        checkName(user);
        if (idNewUser == null) {
            log.info("Add:" + user);
            user.setId(id);
            users.put(id, user);
            id++;
            return ResponseEntity.ok(user);
        } else if(users.containsKey(idNewUser)){
            User oldUser = users.get(idNewUser);
            log.info("Update: " + oldUser + "to:" + user);
            oldUser.setName(user.getName());
            oldUser.setEmail(user.getEmail());
            oldUser.setLogin(user.getLogin());
            oldUser.setBirthday(user.getBirthday());
            return ResponseEntity.ok(oldUser);
        }else {
            log.error("Поступил запрос на редактирование пользователя, которого нет.");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleException(MethodArgumentNotValidException e) {
        log.info(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<String> handleException(ValidationException e) {
        log.info(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
