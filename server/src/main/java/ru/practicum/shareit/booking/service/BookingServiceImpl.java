package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingDTORequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
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
    private final BookingMapper bookingMapper;
    private final Validator validator;

    @Override
    public Collection<BookingDTO> getUserBookings(long userId, BookingState bookingState) {
        validator.checkUserId(userId);
        LocalDateTime currentTime = LocalDateTime.now();
        Collection<Booking> bookings = switch (bookingState) {
            case CURRENT -> bookingRepository.findCurrentBookings(userId, currentTime, currentTime);
            case PAST -> bookingRepository.findPastBookings(userId, currentTime);
            case FUTURE -> bookingRepository.findUpcomingBookings(userId, currentTime);
            case WAITING -> bookingRepository.findBookingsByStatus(userId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findBookingsByStatus(userId, BookingStatus.REJECTED);
            default -> bookingRepository.findAllByBookerId(userId);
        };
        log.info("Получение списка всех бронирований пользователя с id: {}", userId);
        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public BookingDTO getBookingByIdAndUser(long userId, long bookingId) {
        validator.checkUserId(userId);
        Booking booking = validator.validateAndGetBooking(bookingId);
        validator.checkBooker(userId, booking, booking.getItem());
        log.info("Получение бронирования с id: {} пользователя c id: {}", bookingId, userId);
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public Collection<BookingDTO> getOwnerBookings(long userId, BookingState bookingState) {
        validator.checkUserId(userId);
        LocalDateTime currentTime = LocalDateTime.now();
        Collection<Booking> bookings = switch (bookingState) {
            case CURRENT -> bookingRepository.findCurrentItemBookings(userId, currentTime, currentTime);
            case PAST -> bookingRepository.findPastItemBookings(userId, currentTime);
            case FUTURE -> bookingRepository.findUpcomingItemBookings(userId, currentTime);
            case WAITING -> bookingRepository.findItemBookingsByStatus(userId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findItemBookingsByStatus(userId, BookingStatus.REJECTED);
            default -> bookingRepository.findAllByItemOwnerId(userId);
        };
        log.info("получение списка бронирований всех предметов владельца c id: {}", userId);
        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public BookingDTO createBooking(long userId, BookingDTORequest bookingDtoRequest) {
        User booker = validator.validateAndGetUser(userId);
        Item item = validator.validateAndGetItem(bookingDtoRequest.getItemId());
        validator.validateItemAvailability(item);
        Booking booking = bookingMapper.bookingDTORequestToBooking(bookingDtoRequest, item, booker, BookingStatus.WAITING);
        BookingDTO bookingDTO = bookingMapper.toBookingDto(bookingRepository.save(booking));
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
        return bookingMapper.toBookingDto(booking);
    }
}