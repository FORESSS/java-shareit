package ru.practicum.shareit.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.InvalidBookingException;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.exception.PermissionDeniedException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class Validator {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;

    public void checkUserId(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ValidationException(String.format("Пользователь с id: %d не найден", userId));
        }
    }

    public User validateAndGetUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException(String.format("Пользователь с id: %d не найден", userId)));
    }

    public void checkItemId(long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ValidationException(String.format("Предмет с id: %d не найден", itemId));
        }
    }

    public Item validateAndGetItem(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ValidationException(String.format("Предмет с id: %d не найден", itemId)));
    }

    public Booking validateAndGetBooking(long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ValidationException(String.format("Бронирование с id: %d не найдено", bookingId)));
    }

    public ItemRequest validateAndGetRequest(long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new ValidationException(String.format("Запрос вещи с id: %d не найден", requestId)));
    }

    public void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new InvalidRequestException(String.format("Email %s уже используется", email));
        }
    }

    public void checkItemOwner(long userId, Item item) {
        if (userId != item.getOwner().getId()) {
            throw new ValidationException(String.format("Пользователь с id: %d не является владельцем", userId));
        }
    }

    public void checkBookingOwner(long userId, Item item) {
        if (userId != item.getOwner().getId()) {
            throw new PermissionDeniedException(String.format("Пользователь с id: %d не является владельцем", userId));
        }
    }

    public void checkBooker(long userId, Booking booking, Item item) {
        if (userId != booking.getBooker().getId() && userId != item.getOwner().getId()) {
            throw new InvalidBookingException(String.format("Пользователь с id: %d не является владельцем", userId));
        }
    }

   /* public void validateUserHasPastBooking(long userId) {
        boolean hasPastBooking = bookingRepository.existsByBookerIdAndEndBeforeAndStatusNot(
                userId, LocalDateTime.now(), BookingStatus.REJECTED);
        if (!hasPastBooking) {
            throw new PermissionDeniedException(String.format("Невозможно добавить комментарий пользователя с id: %d", userId));
        }
    }*/

    public void validateItemAvailability(Item item) {
        if (!item.getAvailable()) {
            throw new InvalidBookingException(String.format("Предмет с id: %d не доступен для бронирования.", item.getId()));
        }
    }
}