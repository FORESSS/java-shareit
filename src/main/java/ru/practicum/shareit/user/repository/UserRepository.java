package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Collection<User> getAllUsers();

    Optional<User> getUserById(Long userid);

    User createUser(User user);

    User updateUser(Long userId, User user);

    void deleteUserById(Long userId);
}