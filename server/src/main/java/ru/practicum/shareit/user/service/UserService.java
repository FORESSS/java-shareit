package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto user);

    UserDto update(UserDto user, long userId);

    UserDto findById(long id);

    List<UserDto> findAll();

    void delete(long id);
}
