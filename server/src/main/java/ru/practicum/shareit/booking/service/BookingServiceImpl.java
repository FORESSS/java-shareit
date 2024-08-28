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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.Validator;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final Validator validator;

    @Override
    @Transactional(readOnly = true)
    public ResponseBookingDto getBookingById(long userId, long bookingId) {
        Booking booking = validator.validateAndGetBooking(bookingId);
        validator.validateUserAccessToBooking(userId, booking);
        log.info("Пользователь с id: {} запросил информацию о бронировании с id: {}", userId, bookingId);
        return bookingMapper.bookingToResponseBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ResponseBookingDto> getBookingsByBooker(long bookerId, State state) {
        Collection<Booking> bookings = switch (state) {
            case PAST -> bookingRepository.findAllBookingByBookerAndPast(bookerId, LocalDateTime.now());
            case CURRENT -> bookingRepository.findAllBookingByBookerAndCurrent(bookerId, LocalDateTime.now());
            case FUTURE -> bookingRepository.findAllBookingByBookerAndFuture(bookerId, LocalDateTime.now());
            case WAITING -> bookingRepository.findByBookerIdAndStatus(bookerId, Status.WAITING.name());
            case REJECTED -> bookingRepository.findByBookerIdAndStatus(bookerId, Status.REJECTED.name());
            default -> bookingRepository.findByBookerId(bookerId);
        };
        log.info("Пользователь с id: {} запросил список своих бронирований в состоянии: {}", bookerId, state);
        return bookings.stream()
                .map(bookingMapper::bookingToResponseBookingDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ResponseBookingDto> getBookingsByOwner(long ownerId, State state) {
        Collection<Booking> bookings = switch (state) {
            case PAST -> bookingRepository.findAllBookingByOwnerAndPast(ownerId, LocalDateTime.now());
            case CURRENT -> bookingRepository.findAllBookingByOwnerAndCurrent(ownerId, LocalDateTime.now());
            case FUTURE -> bookingRepository.findAllBookingByOwnerAndFuture(ownerId, LocalDateTime.now());
            case WAITING -> bookingRepository.findAllBookingByOwnerAndStatus(ownerId, Status.WAITING.name());
            case REJECTED -> bookingRepository.findAllBookingByOwnerAndStatus(ownerId, Status.REJECTED.name());
            default -> bookingRepository.findAllBookingByOwner(ownerId);
        };
        log.info("Владелец с id: {} запросил список бронирований для своих вещей в состоянии: {}", ownerId, state);
        return bookings.stream()
                .map(bookingMapper::bookingToResponseBookingDto)
                .toList();
    }

    @Override
    public ResponseBookingDto createBooking(long userId, RequestBookingDto creatingBooking) {
        validator.validateBookingDates(creatingBooking);
        User user = validator.validateAndGetUser(userId);
        Item item = validator.validateAndGetItem(creatingBooking.getItemId());
        validator.validateItemAvailability(item);
        Booking booking = bookingMapper.requestBookingDtoToBooking(creatingBooking);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        booking = bookingRepository.save(booking);
        log.info("Пользователь с id: {} создал новое бронирование с id: {} для предмета с id: {}", userId, booking.getId(), item.getId());
        return bookingMapper.bookingToResponseBookingDto(booking);
    }

    @Override
    public ResponseBookingDto updateBooking(long ownerId, long bookingId, boolean approved) {
        Booking booking = validator.validateAndGetBooking(bookingId);
        validator.validateOwnerAccess(ownerId, booking);
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        log.info("Владелец с id: {} {} бронирование с id: {}", ownerId, approved ? "одобрил" : "отклонил", bookingId);
        return bookingMapper.bookingToResponseBookingDto(booking);
    }
}