package ru.practicum.shareit.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class Validator {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public void checkUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ValidationException("Пользователь с id: " + userId + " не найден");
        }
    }

    public void checkItemId(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ValidationException("Предмет с id: " + itemId + " не найден");
        }
    }

    public void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new InvalidRequestException("Email уже используется");
        }
    }

    public void checkEmail(String email, Long userId) {
        if (userRepository.existsByEmail(email, userId)) {
            throw new InvalidRequestException("Email занят");
        }
    }

    public void checkOwner(Long userId, Item item) {
        if (userId != item.getOwner()) {
            throw new ValidationException("Пользователь не является владельцем");
        }
    }
}