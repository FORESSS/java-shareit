package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
   private final UserRepository userRepository;
    @Override
    public Collection<UserDto> getAllUsers() {
                log.info("Возврат списка пользователей");
        return userRepository.getAllUsers()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto getUserById(Long userid) {

        log.info("Возврат пользователя с id: {}",userid);
        return null;
    }

    @Override
    public UserDto createUser(UserDto user) {
        return null;
    }

    @Override
    public UserDto updateUser(Long userId, UserDto user) {
        return null;
    }

    @Override
    public void deleteUserById(Long userId) {

    }
}
