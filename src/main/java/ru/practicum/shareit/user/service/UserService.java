package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAllUsers();

    UserDto getUserById(Long userid);

    UserDto createUser(UserDto user);

    UserDto updateUser(Long userId, UserDto user);

    void deleteUserById(Long userId);
}