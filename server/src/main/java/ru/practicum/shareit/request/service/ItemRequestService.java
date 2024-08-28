package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto getRequestById(long requestId);

    Collection<ItemRequestDto> getRequestsByUserId(long userId);

    Collection<ItemRequestDto> getAllRequests(long userId);

    ItemRequestDto createRequest(long userId, ItemRequestDto request);
}