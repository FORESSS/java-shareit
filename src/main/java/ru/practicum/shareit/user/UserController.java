package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable @NotNull Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid UserDto user) {
        return userService.createUser(user);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@PathVariable @NotNull Long userId, @RequestBody @Valid UserDto user) {
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable @NotNull Long userId) {
        userService.deleteUserById(userId);
    }
}