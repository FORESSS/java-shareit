package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.service.BookingClient;

import static ru.practicum.shareit.util.Constants.USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("bookings")
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(USER_ID) @Positive long userId,
                                                 @PathVariable @Positive long bookingId) {
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByBooker(@RequestHeader(USER_ID) @Positive long bookerId,
                                                      @RequestParam(defaultValue = "ALL") State state) {
        return bookingClient.getBookingsByBooker(bookerId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(@RequestHeader(USER_ID) @Positive long ownerId,
                                                     @RequestParam(defaultValue = "ALL") State state) {
        return bookingClient.getBookingsByOwner(ownerId, state);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(USER_ID) @Positive long userId,
                                                @RequestBody @Valid RequestBookingDto booking) {
        return bookingClient.createBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader(USER_ID) @Positive long ownerId,
                                                @PathVariable @Positive long bookingId, @RequestParam boolean approved) {
        return bookingClient.updateBooking(ownerId, bookingId, approved);
    }
}