package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserClient;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        log.info("Получен GET запрос на получение всех пользователей");
        return userClient.findAll();
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto user) {
        log.info("Получен POST запрос на создание пользователя user {}", user);
        return userClient.create(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@RequestBody UserDto newUser, @PathVariable long userId) {
        log.info("Получен PATCH запрос на обновление пользователя с userId = {}, поля, которые нужно обновить: {}",
                userId, newUser);
        return userClient.update(userId, newUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable long userId) {
        log.info("Получен DELETE запрос на удаление пользователя с userId = {}", userId);
        return userClient.delete(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findById(@PathVariable long userId) {
        log.info("Получен GET запрос на получения пользователя с userId = {}", userId);
        return userClient.findById(userId);
    }
}