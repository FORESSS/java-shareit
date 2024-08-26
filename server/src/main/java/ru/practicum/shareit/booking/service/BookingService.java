package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    ResponseBookingDto create(RequestBookingDto booking, long id);

    ResponseBookingDto update(long bookingId, long ownerId, boolean approved);

    ResponseBookingDto findById(long bookingId, long userId);

    List<ResponseBookingDto> findByBooker(long bookerId, State state);

    List<ResponseBookingDto> findByOwner(long ownerId, State state);
}
