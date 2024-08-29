package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestClient;

import static ru.practicum.shareit.util.Constants.USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("requests")
public class ItemRequestController {
    private final RequestClient requestClient;

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getRequestById(@PathVariable @Positive long requestId) {
        return requestClient.getRequestById(requestId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getRequestsByUserId(@RequestHeader(USER_ID) @Positive long userId) {
        return requestClient.getRequestsByUserId(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllRequests(@RequestHeader(USER_ID) @Positive long userId) {
        return requestClient.getAllRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createRequest(@RequestHeader(USER_ID) @Positive long userId,
                                                @RequestBody @Valid ItemRequestDto request) {
        return requestClient.createRequest(userId, request);
    }
}