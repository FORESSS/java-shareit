package ru.practicum.shareit.request.RequestService;

import ru.practicum.shareit.request.dto.ItemRequestDTO;

import java.util.Collection;

public interface RequestService {
    Collection<ItemRequestDTO> getItemRequestsByUserId(long userId);

    Collection<ItemRequestDTO> getAllItemRequests(long userId);

    ItemRequestDTO getItemRequestById(long userId, long requestId);

    ItemRequestDTO createItemRequest(long userId, ItemRequestDTO requestDto);
}