package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto create(@RequestBody ItemRequestDto request, @RequestHeader(USER_ID) long userId) {
        log.info("Получен POST запрос на создание запроса {} пользователем с userId = {}", request, userId);
        return itemRequestService.create(request, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findByRequestId(@PathVariable long requestId) {
        log.info("Получен GET запрос на получение запроса с requestId = {}", requestId);
        return itemRequestService.findByRequestId(requestId);
    }

    @GetMapping
    public List<ItemRequestDto> findByUserId(@RequestHeader(USER_ID) long userId) {
        log.info("Получен GET запрос на получение всех запросов пользователя с userId = {}", userId);
        return itemRequestService.findByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAll(@RequestHeader(USER_ID) long userId) {
        log.info("Получен GET запрос на получение всех запросов, созданных другими пользователями. userId = {}", userId);
        return itemRequestService.findAll(userId);
    }
}