package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;

import java.util.Collection;

public interface UserService {

    Collection<UserDTO> getAllUsers();

    UserDTO getUserById(Long userid);

    UserDTO createUser(UserDTO user);

    UserDTO updateUser(Long userId, UserUpdateDTO user);

    void deleteUserById(Long userId);
}