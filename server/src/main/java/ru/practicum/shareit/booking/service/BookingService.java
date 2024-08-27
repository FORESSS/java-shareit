package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.State;

import java.util.Collection;

public interface BookingService {
    ResponseBookingDto getBookingById(long userId, long bookingId);

    Collection<ResponseBookingDto> getBookingsByBooker(long bookerId, State state);

    Collection<ResponseBookingDto> getBookingsByOwner(long ownerId, State state);

    ResponseBookingDto createBooking(long id, RequestBookingDto booking);

    ResponseBookingDto updateBooking(long ownerId, long bookingId, boolean approved);
}