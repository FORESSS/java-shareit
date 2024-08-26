package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.model.ItemRequest;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {
    ItemRequestDTO toItemRequestDto(ItemRequest itemRequest);

    ItemRequest toItemRequest(ItemRequestDTO itemRequestDto);
}