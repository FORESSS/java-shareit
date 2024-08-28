package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

import static ru.practicum.shareit.util.Constants.USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@PathVariable long requestId) {
        return itemRequestService.getRequestById(requestId);
    }

    @GetMapping
    public Collection<ItemRequestDto> getRequestsByUserId(@RequestHeader(USER_ID) long userId) {
        return itemRequestService.getRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllRequests(@RequestHeader(USER_ID) long userId) {
        return itemRequestService.getAllRequests(userId);
    }

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader(USER_ID) long userId, @RequestBody ItemRequestDto request) {
        return itemRequestService.createRequest(userId, request);
    }
}