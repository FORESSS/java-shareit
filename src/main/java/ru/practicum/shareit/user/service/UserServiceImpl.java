package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.Validator;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Validator validator;

    @Override
    public Collection<UserDTO> getAllUsers() {
        log.info("Получение списка всех пользователей");
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDTO getUserById(long userId) {
        User user = validator.validateAndGetUser(userId);
        log.info("Получение пользователя с id: {}", userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDTO add(UserDTO userDto) {
        if (userRepository.findAll().stream().anyMatch(user -> user.getEmail().equals(userDto.getEmail()))) {
            throw new ConException(String.format("Failed to add user with email %s, email is not unique.", userDto.getEmail()));
        }
        User user = userRepository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDTO updateUser(long userId, UserDTO userDto) {
        User user = validator.validateAndGetUser(userId);
        String newEmail = userDto.getEmail();
        if (newEmail != null) {
            validator.checkEmail(newEmail);
            user.setEmail(newEmail);
        }
        String newName = userDto.getName();
        if (newName != null && !newName.isBlank()) {
            user.setName(newName);
        }
        userRepository.save(user);
        log.info("Обновление пользователя с id: {}", userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUserById(long userId) {
        validator.checkUserId(userId);
        log.info("Удаление пользователя с id: {}", userId);
        userRepository.deleteById(userId);
    }
}