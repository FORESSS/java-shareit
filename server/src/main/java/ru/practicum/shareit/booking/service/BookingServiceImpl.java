package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.PermissionDeniedException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.Validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;
    private final Validator validator;

    @Override
    public ResponseBookingDto create(RequestBookingDto creatingBooking, long userId) {
        log.info("Начало процесса создания бронирования с userId = {}", userId);

        if (creatingBooking.getStart().isAfter(creatingBooking.getEnd())) {
            throw new ValidationException("Время старта бронирование должно быть до времени конца бронирования");
        }

        User user = validator.validateAndGetUser(userId);
        Item item = validator.validateAndGetItem(creatingBooking.getItemId());

        if (!item.getAvailable()) {
            throw new ValidationException("Предмет недоступен для бронирования");
        }

        Booking booking = bookingMapper.requestBookingDtoToBooking(creatingBooking);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        booking = bookingRepository.save(booking);
        log.info("Бронирование создано");
        return bookingMapper.bookingToResponseBookingDto(booking);
    }

    @Override
    public ResponseBookingDto update(long bookingId, long ownerId, boolean approved) {
        log.info("Начало процесса подтверждения бронирования с ownerId = {}, approved = {}", ownerId, approved);
        Booking booking = validator.validateAndGetBooking(bookingId);

        if (booking.getItem().getOwner().getId() == ownerId) {
            booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        } else {
            throw new PermissionDeniedException("У вас нет доступа к этой информации");
        }

        log.info("Статус бронирования изменен на approved = {}", approved);
        return bookingMapper.bookingToResponseBookingDto(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseBookingDto findById(long id, long userId) {
        log.info("Начало процесса получения бронирования с id = {} от пользователя с userId = {}", id, userId);
        Booking booking = validator.validateAndGetBooking(id);

        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new ValidationException("У вас нет доступа к этой информации");
        }

        log.info("Бронирование получено");
        return bookingMapper.bookingToResponseBookingDto(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ResponseBookingDto> findByBooker(long bookerId, State state) {
        log.info("Начало процесса получения списка всех бронирований пользователя с bookerId = {}, " +
                "со статусом state = {}", bookerId, state);
        List<Booking> bookings;

        switch (state) {
            case ALL -> bookings = bookingRepository.findByBookerId(bookerId);
            case PAST -> bookings = bookingRepository.findAllBookingByBookerAndPast(bookerId, LocalDateTime.now());
            case CURRENT -> bookings = bookingRepository.findAllBookingByBookerAndCurrent(bookerId, LocalDateTime.now());
            case FUTURE -> bookings = bookingRepository.findAllBookingByBookerAndFuture(bookerId, LocalDateTime.now());
            case WAITING -> bookings = bookingRepository.findByBooker_idAndStatus(bookerId, Status.WAITING.name());
            case REJECTED -> bookings = bookingRepository.findByBooker_idAndStatus(bookerId, Status.REJECTED.name());
            case null, default -> bookings = new ArrayList<>();
        }

        log.info("Список бронирований пользователя получен");
        return bookings.stream()
                .map(bookingMapper::bookingToResponseBookingDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ResponseBookingDto> findByOwner(long ownerId, State state) {
        log.info("Начало процесса получения списка всех бронирований вещей пользователя с ownerId = {}, " +
                "со статусом state = {}", ownerId, state);
        List<Booking> bookings;

        switch (state) {
            case ALL -> bookings = bookingRepository.findAllBookingByOwner(ownerId);
            case PAST -> bookings = bookingRepository.findAllBookingByOwnerAndPast(ownerId, LocalDateTime.now());
            case CURRENT -> bookings = bookingRepository.findAllBookingByOwnerAndCurrent(ownerId, LocalDateTime.now());
            case FUTURE -> bookings = bookingRepository.findAllBookingByOwnerAndFuture(ownerId, LocalDateTime.now());
            case WAITING -> bookings = bookingRepository.findAllBookingByOwnerAndStatus(ownerId, Status.WAITING.name());
            case REJECTED -> bookings = bookingRepository
                    .findAllBookingByOwnerAndStatus(ownerId, Status.REJECTED.name());
            case null, default -> bookings = new ArrayList<>();
        }

        if (bookings.isEmpty()) {
            throw new ValidationException("Список пустой");
        }

        log.info("Список бронирований вещей пользователя получен");
        return bookings.stream()
                .map(bookingMapper::bookingToResponseBookingDto)
                .toList();
    }
}