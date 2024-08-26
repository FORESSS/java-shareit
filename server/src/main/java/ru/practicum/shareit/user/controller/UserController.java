package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> findAll() {
        log.info("Получен GET запрос на получение всех пользователей");
        return userService.findAll();
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto user) {
        log.info("Получен POST запрос на создание пользователя user {}", user);
        return userService.create(user);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto newUser, @PathVariable long userId) {
        log.info("Получен PATCH запрос на обновление пользователя с userId = {}, поля, которые нужно обновить: {}",
                userId, newUser);
        return userService.update(newUser, userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        log.info("Получен DELETE запрос на удаление пользователя с userId = {}", userId);
        userService.delete(userId);
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable long userId) {
        log.info("Получен GET запрос на получения пользователя с userId = {}", userId);
        return userService.findById(userId);
    }
}