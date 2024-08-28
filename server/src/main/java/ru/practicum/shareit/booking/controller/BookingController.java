package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

import static ru.practicum.shareit.util.Constants.USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public ResponseBookingDto getBookingById(@RequestHeader(USER_ID) long userId, @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public Collection<ResponseBookingDto> getBookingsByBooker(@RequestHeader(USER_ID) long bookerId,
                                                              @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getBookingsByBooker(bookerId, state);
    }

    @GetMapping("/owner")
    public Collection<ResponseBookingDto> getBookingsByOwner(@RequestHeader(USER_ID) long ownerId,
                                                             @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getBookingsByOwner(ownerId, state);
    }

    @PostMapping
    public ResponseBookingDto createBooking(@RequestHeader(USER_ID) long userId,
                                            @RequestBody RequestBookingDto booking) {
        return bookingService.createBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseBookingDto updateBooking(@RequestHeader(USER_ID) long ownerId, @PathVariable long bookingId,
                                            @RequestParam boolean approved) {
        return bookingService.updateBooking(ownerId, bookingId, approved);
    }
}