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

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Validator validator;

    @Override
    public UserDto create(UserDto user) {
        log.info("Начало процесса создания пользователя");
        User createdUser = userRepository.save(userMapper.userDtoToUser(user));
        log.info("Пользователь создан");
        return userMapper.userToUserDto(createdUser);
    }

    @Override
    public UserDto update(UserDto newUser, long userId) {
        log.info("Начало процесса обновления пользователя с userId = {}", userId);
        User updatedUser = validator.validateAndGetUser(userId);
        if (newUser.getName() != null && !newUser.getName().isBlank()) {
            updatedUser.setName(newUser.getName());
        }
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
            updatedUser.setEmail(newUser.getEmail());
        }
        log.info("Пользователь обновлен");
        return userMapper.userToUserDto(updatedUser);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto findById(long id) {
        log.info("Начало процесса получения пользователя по id = {}", id);
        User user = validator.validateAndGetUser(id);
        log.info("Пользователь получен");
        return userMapper.userToUserDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAll() {
        log.info("Начало процесса получения всех пользователей");
        List<User> users = userRepository.findAll();
        log.info("Список пользователей получен");
        return users.stream()
                .map(userMapper::userToUserDto)
                .toList();
    }

    @Override
    public void delete(long id) {
        log.info("Начало процесса удаления пользователя по id = {}", id);

        validator.checkUserId(id);

        userRepository.deleteById(id);
        log.info("Пользователь удален");
    }
}
