package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("bookings")
public class BookingController {
    public static final String USER_ID = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @PostMapping
    public ResponseBookingDto create(@RequestBody RequestBookingDto booking,
                                     @RequestHeader(USER_ID) long userId) {
        log.info("Получен POST запрос на создание бронирования {} пользователем с id = {}", booking, userId);
        return bookingService.create(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseBookingDto update(@PathVariable long bookingId,
                                     @RequestHeader(USER_ID) long ownerId,
                                     @RequestParam boolean approved) {
        log.info("Получен PATCH запрос на подтверждение или отклонение запроса с bookingId = {} на бронирование " +
                "от владельца с ownerId = {}, статус подтверждения: {}", bookingId, ownerId, approved);
        return bookingService.update(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseBookingDto findById(@PathVariable long bookingId,
                                       @RequestHeader(USER_ID) long userId) {
        log.info("Получен GET запрос на получение бронирования с bookingId = {}, от пользователя с id = {}",
                bookingId, userId);
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    public List<ResponseBookingDto> findByBooker(@RequestHeader(USER_ID) long bookerId,
                                                 @RequestParam(defaultValue = "ALL") State state) {
        log.info("Получен GET запрос на получение всех бронирований текущего пользователя с bookerId = {}, state = {}",
                bookerId, state);
        return bookingService.findByBooker(bookerId, state);
    }

    @GetMapping("/owner")
    public List<ResponseBookingDto> findByOwner(@RequestHeader(USER_ID) long ownerId,
                                                @RequestParam(defaultValue = "ALL") State state) {
        log.info("Получен GET запрос на получение списка бронирований для всех вещей текущего пользователя " +
                        "с ownerId = {}, state = {}", ownerId, state);
        return bookingService.findByOwner(ownerId, state);
    }
}
