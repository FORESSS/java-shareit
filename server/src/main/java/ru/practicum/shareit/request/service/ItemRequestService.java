package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto newItemRequest, long userId);

    List<ItemRequestDto> findByUserId(long userId);

    List<ItemRequestDto> findAll(long userId);

    ItemRequestDto findByRequestId(long requestId);
}
