package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDTO;

import java.util.Collection;

public interface UserService {
    Collection<UserDTO> getAllUsers();

    UserDTO getUserById(long userid);

    UserDTO add(UserDTO userDto);

    UserDTO updateUser(long userId, UserDTO user);

    void deleteUserById(long userId);
}