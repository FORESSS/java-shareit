package ru.practicum.shareit.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.InvalidBookingException;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;


@Component
@RequiredArgsConstructor
public class Validator {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

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

    public void checkBookingId(Long itemId) {
        if (!bookingRepository.existsById(itemId)) {
            throw new ValidationException("Бронирование с id: " + itemId + " не найдено");
        }
    }

    public void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new InvalidRequestException("Email уже используется");
        }
    }

    public void checkOwner(Long userId, Item item)  {
        if (userId != item.getOwner().getId()) {
            throw new AccessException("Пользователь не является владельцем1");
        }
    }

    public void checkBooker(Long userId, Booking booking, Item item) {
        if (userId != booking.getBooker().getId() && userId != item.getOwner().getId()) {
            throw new InvalidBookingException("Пользователь не является владельцем2");
        }
    }
}