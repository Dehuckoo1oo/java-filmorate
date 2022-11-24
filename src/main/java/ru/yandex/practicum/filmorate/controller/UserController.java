package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.*;

@Validated
@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/users")
    public ResponseEntity<User> delete(@Valid @RequestBody User user) {
        Optional<User> entity = userService.delete(user);
        return entity.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> findUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/users/{id}/friends")
    public ResponseEntity<List<User>> findFriendList(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getFriendList(id));
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> findMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return ResponseEntity.ok(userService.mutualFriends(id, otherId));
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity<User> deleteFromFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return ResponseEntity.ok(userService.deleteFriends(id, friendId));
    }

    @GetMapping("/users")
    public List<User> findAll() {
        return userService.get();
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.create(user));
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        Long idNewUser = user.getId();
        if (idNewUser == null) {
            return ResponseEntity.ok(userService.create(user));
        } else {
            return userService.update(user)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity<User> addToFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return ResponseEntity.ok(userService.addFriends(id, friendId));
    }
}
