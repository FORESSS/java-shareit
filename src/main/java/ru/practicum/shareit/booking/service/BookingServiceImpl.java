package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingDTORequest;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.Validator;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final Validator validator;

    @Override
    public Collection<BookingDTO> getUserBookings(long userId, BookingState bookingState) {
        validator.checkUserId(userId);
        LocalDateTime currentTime = LocalDateTime.now();
        Collection<Booking> bookings = switch (bookingState) {
            case CURRENT ->
                    bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, currentTime, currentTime);
            case PAST -> bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, currentTime);
            case FUTURE -> bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, currentTime);
            case WAITING -> bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED ->
                    bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
            default -> bookingRepository.findAllByBookerId(userId);
        };
        log.info("Получение списка всех бронирований пользователя с id: {}", userId);
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public BookingDTO getBookingByIdAndUser(long userId, long bookingId) {
        validator.checkUserId(userId);
        Booking booking = validator.validateAndGetBooking(bookingId);
        validator.checkBooker(userId, booking, booking.getItem());
        log.info("Получение бронирования с id: {} пользователя c id: {}", bookingId, userId);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public Collection<BookingDTO> getOwnerBookings(long userId, BookingState bookingState) {
        validator.checkUserId(userId);
        LocalDateTime currentTime = LocalDateTime.now();
        Collection<Booking> bookings = switch (bookingState) {
            case CURRENT ->
                    bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, currentTime, currentTime);
            case PAST -> bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, currentTime);
            case FUTURE -> bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, currentTime);
            case WAITING ->
                    bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED ->
                    bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
            default -> bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
        };
        log.info("получение списка бронирований всех предметов владельца c id: {}", userId);
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public BookingDTO createBooking(long userId, BookingDTORequest bookingDtoRequest) {
        User booker = validator.validateAndGetUser(userId);
        Item item = validator.validateAndGetItem(bookingDtoRequest.getItemId());
        validator.validateItemAvailability(item);
        Booking booking = BookingMapper.toBookingFromBookingRequest(bookingDtoRequest, item, booker, BookingStatus.WAITING);
        BookingDTO bookingDTO = BookingMapper.toBookingDto(bookingRepository.save(booking));
        log.info("Создание нового бронирования с id: {} пользователя с id: {}", booking.getId(), userId);
        return bookingDTO;
    }

    @Override
    public BookingDTO approveBooking(long userId, long bookingId, boolean approved) {
        Booking booking = validator.validateAndGetBooking(bookingId);
        validator.checkBookingOwner(userId, booking.getItem());
        BookingStatus newStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(newStatus);
        if (approved) {
            log.info("Бронирование с id: {} было подтверждено", bookingId);
        } else {
            log.info("Бронирование с id {} было отклонено", bookingId);
        }
        return BookingMapper.toBookingDto(booking);
    }
}