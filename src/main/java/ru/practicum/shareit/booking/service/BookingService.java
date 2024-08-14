package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingDTORequest;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

public interface BookingService {
    Collection<BookingDTO> getAllBookings(long userId, BookingState bookingState);

    BookingDTO getBookingById(long userId, long bookingId);

    Collection<BookingDTO> getAllByOwner(long userId, BookingState bookingState);

    BookingDTO createBooking(long bookerId, BookingDTORequest bookingDtoRequest);

    BookingDTO approveBooking(long userId, long bookingId, boolean approved);
}