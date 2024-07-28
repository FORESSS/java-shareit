package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDTO;
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
        log.info("Возврат списка всех пользователей");
        return userRepository.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDTO getUserById(Long userId) {
        validator.checkUserId(userId);
        log.info("Возврат пользователя с id: {}", userId);
        return userRepository.getUserById(userId)
                .map(UserMapper::toUserDto)
                .orElseThrow(() -> new ValidationException("Пользователь с id: " + userId + " не найден"));
    }

    @Override
    public UserDTO createUser(UserDTO userDto) {
        validator.checkEmail(userDto.getEmail());
        User user = UserMapper.toUser(userDto);
        log.info("Создание нового пользователя с id: {}", user.getId());
        return UserMapper.toUserDto(userRepository.createUser(user));
    }

    @Override
    public UserDTO updateUser(Long userId, UserUpdateDTO userDto) {
        validator.checkUserId(userId);
        validator.checkEmail(userDto.getEmail(), userId);
        User user = userRepository.getUserById(userId).orElseThrow(() ->
                new ValidationException("Пользователь с id: " + userId + " не найден"));
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        log.info("Обновление пользователя с id: {}", userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUserById(Long userId) {
        validator.checkUserId(userId);
        log.info("Удаление пользователя с id: {}", userId);
        userRepository.deleteUserById(userId);
    }
}