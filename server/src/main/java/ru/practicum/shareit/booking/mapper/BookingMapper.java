package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingDTORequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingDTO toBookingDto(Booking booking);

    default Booking bookingDTORequestToBooking(BookingDTORequest bookingDtoRequest,
                                               Item item, User booker, BookingStatus status) {
        return Booking.builder()
                .id(bookingDtoRequest.getItemId())
                .start(bookingDtoRequest.getStart())
                .end(bookingDtoRequest.getEnd())
                .item(item)
                .booker(booker)
                .status(status)
                .build();
    }
}