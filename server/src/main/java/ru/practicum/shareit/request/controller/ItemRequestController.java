package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.RequestService.RequestService;
import ru.practicum.shareit.request.dto.ItemRequestDTO;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final RequestService requestService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @GetMapping
    public Collection<ItemRequestDTO> getItemRequestsByUserId(@RequestHeader(USER_ID_HEADER) long userId) {
        return requestService.getItemRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDTO> getAllItemRequests(@RequestHeader(USER_ID_HEADER) long userId) {
        return requestService.getAllItemRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDTO getItemRequestById(@RequestHeader(USER_ID_HEADER) long userId,
                                             @PathVariable long requestId) {
        return requestService.getItemRequestById(userId, requestId);
    }

    @PostMapping
    public ItemRequestDTO createItemRequest(@RequestHeader(USER_ID_HEADER) long userId,
                                            @RequestBody ItemRequestDTO requestDto) {
        return requestService.createItemRequest(userId, requestDto);
    }
}