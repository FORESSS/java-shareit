package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAllUsers();

    UserDto getUserById(long userId);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(long userId, UserDto userDto);

    void deleteUser(long userId);
}