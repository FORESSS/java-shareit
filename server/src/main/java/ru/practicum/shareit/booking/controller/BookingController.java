package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingDTORequest;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookingDTO> getUserBookings(@RequestHeader(USER_ID_HEADER) long userId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") BookingState bookingState) {
        return bookingService.getUserBookings(userId, bookingState);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDTO getBookingByIdAndUser(@RequestHeader(USER_ID_HEADER) long userId,
                                            @PathVariable Long bookingId) {
        return bookingService.getBookingByIdAndUser(userId, bookingId);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookingDTO> getOwnerBookings(@RequestHeader(USER_ID_HEADER) long userId,
                                                   @RequestParam(name = "state", defaultValue = "ALL") BookingState bookingState) {
        return bookingService.getOwnerBookings(userId, bookingState);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDTO createBooking(@RequestHeader(USER_ID_HEADER) long bookerId,
                                    @RequestBody BookingDTORequest bookingDtoRequest) {
        return bookingService.createBooking(bookerId, bookingDtoRequest);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDTO approveBooking(@RequestHeader(USER_ID_HEADER) long userId,
                                     @PathVariable long bookingId, @RequestParam boolean approved) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }
}