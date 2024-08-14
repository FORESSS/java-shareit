package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    public Collection<BookingDTO> getAllBookings(@RequestHeader(USER_ID_HEADER) @Positive long userId,
                                                 @RequestParam(name = "state", defaultValue = "ALL") BookingState bookingState) {
        return bookingService.getAllBookings(userId, bookingState);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDTO getBookingById(@RequestHeader(USER_ID_HEADER) @Positive long userId,
                                     @PathVariable @NotNull Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookingDTO> getAllByOwner(@RequestHeader(USER_ID_HEADER) @Positive long userId,
                                                @RequestParam(name = "state", defaultValue = "ALL") BookingState bookingState) {
        return bookingService.getAllByOwner(userId, bookingState);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDTO createBooking(@RequestHeader(USER_ID_HEADER) @Positive long bookerId,
                                    @RequestBody @Valid BookingDTORequest bookingDtoRequest) {
        return bookingService.createBooking(bookerId, bookingDtoRequest);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDTO approveBooking(@RequestHeader(USER_ID_HEADER) @Positive long userId,
                                     @PathVariable @Positive long bookingId, @RequestParam boolean approved) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }
}