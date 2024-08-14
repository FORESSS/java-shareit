package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingDTORequest;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.InvalidBookingException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.Validator;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final Validator validator;
    private final UserRepository userRepository;

    @Override
    public List<BookingDTO> getAllBookings(long userId, BookingState bookingState) {
        validator.checkUserId(userId);
        LocalDateTime currentTime = LocalDateTime.now();

        List<Booking> bookings = switch (bookingState) {
            case ALL -> bookingRepository.findAllByBookerId(userId);
            case CURRENT ->
                    bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, currentTime, currentTime);
            case PAST -> bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, currentTime);
            case FUTURE -> bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, currentTime);
            case WAITING -> bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED -> throw new InvalidBookingException("Неверное состояние бронирования.");
        };

        return bookings.stream().map(BookingMapper::toBookingDto).toList();
    }

    @Override
    public BookingDTO getBookingById(long userId, long bookingId) {
        validator.checkUserId(userId);
        validator.checkBookingId(bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ValidationException("Бронирование не найдено"));

        validator.checkBooker(userId, booking, booking.getItem());

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDTO> getAllByOwner(long userId, BookingState bookingState) {
        validator.checkUserId(userId);
        LocalDateTime currentTime = LocalDateTime.now();

        List<Booking> bookings = switch (bookingState) {
            case ALL -> bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
            case CURRENT ->
                    bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, currentTime, currentTime);
            case PAST -> bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, currentTime);
            case FUTURE -> bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, currentTime);
            case WAITING ->
                    bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED -> throw new InvalidBookingException("Неверное состояние бронирования.");
        };

        return bookings.stream().map(BookingMapper::toBookingDto).toList();
    }

    @Override
    public BookingDTO createBooking(long bookerId, BookingDTORequest bookingDtoRequest) {
        validator.checkUserId(bookerId);

        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new ValidationException("Пользователь не найден"));
        Item item = itemRepository.findById(bookingDtoRequest.getItemId())
                .orElseThrow(() -> new ValidationException("Предмет не найден"));

        if (!item.getAvailable()) {
            throw new InvalidBookingException(String.format("Предмет с id %d недоступен для бронирования.", item.getId()));
        }

        Booking booking = BookingMapper.toBookingFromBookingRequest(bookingDtoRequest, item, booker, BookingStatus.WAITING);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDTO approveBooking(long userId, long bookingId, boolean approved) {
        validator.checkBookingId(bookingId);
        Booking booking = bookingRepository.findById(bookingId).get();

        validator.checkOwner(userId, booking.getItem());
        BookingStatus newStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(newStatus);
        return BookingMapper.toBookingDto(booking);
    }

}