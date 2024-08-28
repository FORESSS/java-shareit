package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.Validator;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Validator validator;

    @Override
    @Transactional(readOnly = true)
    public Collection<UserDto> getAllUsers() {
        log.info("Получение списка всех пользователей");
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(long userId) {
        User user = validator.validateAndGetUser(userId);
        log.info("Получение пользователя с id: {}", userId);
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        validator.checkEmail(userDto.getEmail());
        User user = userMapper.userDtoToUser(userDto);
        userRepository.save(user);
        log.info("Создание нового пользователя с id: {}", user.getId());
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
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
        return userMapper.userToUserDto(user);
    }

    @Override
    public void deleteUser(long userId) {
        validator.checkUserId(userId);
        log.info("Удаление пользователя с id: {}", userId);
        userRepository.deleteById(userId);
    }
}