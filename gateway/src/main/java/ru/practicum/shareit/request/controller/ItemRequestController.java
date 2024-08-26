package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestClient;

import static ru.practicum.shareit.constants.Constants.USER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("requests")
public class ItemRequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemRequestDto request, @RequestHeader(USER_ID) long userId) {
        log.info("Получен POST запрос на создание запроса {} пользователем с userId = {}", request, userId);
        return requestClient.create(userId, request);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findByRequestId(@PathVariable long requestId) {
        log.info("Получен GET запрос на получение запроса с requestId = {}", requestId);
        return requestClient.findByRequestId(requestId);
    }

    @GetMapping
    public ResponseEntity<Object> findByUserId(@RequestHeader(USER_ID) long userId) {
        log.info("Получен GET запрос на получение всех запросов пользователя с userId = {}", userId);
        return requestClient.findByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@RequestHeader(USER_ID) long userId) {
        log.info("Получен GET запрос на получение всех запросов, созданных другими пользователями. userId = {}", userId);
        return requestClient.findAll(userId);
    }
}