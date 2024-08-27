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
import java.util.Collection;

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
    public ResponseBookingDto createBooking(long userId, RequestBookingDto creatingBooking) {
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
    public ResponseBookingDto updateBooking(long ownerId, long bookingId, boolean approved) {
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
    public ResponseBookingDto getBookingById(long userId, long bookingId) {

        Booking booking = validator.validateAndGetBooking(bookingId);

        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new PermissionDeniedException("У вас нет доступа к этой информации");
        }

        log.info("Бронирование получено");
        return bookingMapper.bookingToResponseBookingDto(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<ResponseBookingDto> getBookingsByBooker(long bookerId, State state) {
        log.info("Начало процесса получения списка всех бронирований пользователя с bookerId = {}, " +
                "со статусом state = {}", bookerId, state);
        Collection<Booking> bookings = switch (state) {

            case PAST -> bookingRepository.findAllBookingByBookerAndPast(bookerId, LocalDateTime.now());
            case CURRENT -> bookingRepository.findAllBookingByBookerAndCurrent(bookerId, LocalDateTime.now());
            case FUTURE -> bookingRepository.findAllBookingByBookerAndFuture(bookerId, LocalDateTime.now());
            case WAITING -> bookingRepository.findByBooker_idAndStatus(bookerId, Status.WAITING.name());
            case REJECTED -> bookingRepository.findByBooker_idAndStatus(bookerId, Status.REJECTED.name());
            default -> bookingRepository.findByBookerId(bookerId);
        };

        log.info("Список бронирований пользователя получен");
        return bookings.stream()
                .map(bookingMapper::bookingToResponseBookingDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<ResponseBookingDto> getBookingsByOwner(long ownerId, State state) {
        log.info("Начало процесса получения списка всех бронирований вещей пользователя с ownerId = {}, " +
                "со статусом state = {}", ownerId, state);
        Collection<Booking> bookings = switch (state) {

            case PAST -> bookingRepository.findAllBookingByOwnerAndPast(ownerId, LocalDateTime.now());
            case CURRENT -> bookingRepository.findAllBookingByOwnerAndCurrent(ownerId, LocalDateTime.now());
            case FUTURE -> bookingRepository.findAllBookingByOwnerAndFuture(ownerId, LocalDateTime.now());
            case WAITING -> bookingRepository.findAllBookingByOwnerAndStatus(ownerId, Status.WAITING.name());
            case REJECTED -> bookingRepository
                    .findAllBookingByOwnerAndStatus(ownerId, Status.REJECTED.name());
            default -> bookingRepository.findAllBookingByOwner(ownerId);
        };

        if (bookings.isEmpty()) {
            throw new ValidationException("Список пустой");
        }

        log.info("Список бронирований вещей пользователя получен");
        return bookings.stream()
                .map(bookingMapper::bookingToResponseBookingDto)
                .toList();
    }
}