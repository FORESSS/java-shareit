package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
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
        log.info("Возврат списка всех пользователей");
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDTO getUserById(long userId) {
        validator.checkUserId(userId);
        log.info("Возврат пользователя с id: {}", userId);
        return userRepository.findById(userId)
                .map(UserMapper::toUserDto)
                .orElseThrow(() -> new ValidationException("Пользователь с id: " + userId + " не найден"));
    }

    @Override
    public UserDTO createUser(UserDTO userDto) {
        validator.checkEmail(userDto.getEmail());
        User user = UserMapper.toUser(userDto);
        userRepository.save(user);
        log.info("Создание нового пользователя с id: {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDTO updateUser(long userId, UserDTO user) {
        validator.checkUserId(userId);
        User oldUser = userRepository.findById(userId).get();

        String newEmail = user.getEmail();
        if (newEmail != null && !newEmail.equals(oldUser.getEmail())) {
            validator.checkEmail(user.getEmail());
            oldUser.setEmail(newEmail);
        }

        String newName = user.getName();
        if (newName != null) {
            oldUser.setName(newName);
        }

        User user1 = userRepository.save(oldUser);
        return UserMapper.toUserDto(user1);
    }

    @Override
    public void deleteUserById(long userId) {
        validator.checkUserId(userId);
        log.info("Удаление пользователя с id: {}", userId);
        userRepository.deleteById(userId);
    }
}