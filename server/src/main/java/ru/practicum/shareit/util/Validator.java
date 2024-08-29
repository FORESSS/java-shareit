package ru.practicum.shareit.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
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

import java.time.LocalDateTime;

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

    public void checkUserHasBookings(long userId) {
        if (bookingRepository.findByUserId(userId, LocalDateTime.now()).isEmpty()) {
            throw new InvalidBookingException("Данный пользователь не использовал эту вещь.");
        }
    }

    public void validateBookingDates(RequestBookingDto creatingBooking) {
        if (creatingBooking.getStart().isAfter(creatingBooking.getEnd())) {
            throw new InvalidBookingException("Время старта бронирования должно быть до времени конца бронирования");
        }
    }

    public void validateItemAvailability(Item item) {
        if (!item.getAvailable()) {
            throw new InvalidBookingException("Предмет недоступен для бронирования");
        }
    }

    public void validateOwnerAccess(long ownerId, Booking booking) {
        if (booking.getItem().getOwner().getId() != ownerId) {
            throw new PermissionDeniedException("У вас нет доступа к этой информации");
        }
    }

    public void validateUserAccessToBooking(long userId, Booking booking) {
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new PermissionDeniedException("У вас нет доступа к этой информации");
        }
    }
}