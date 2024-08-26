package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDateDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto item, long userId);

    CommentDto createComment(CommentDto comment, long itemId, long userId);

    ItemDto update(ItemDto itemDto, long id, long ownerId);

    ItemDto findById(long id);

    List<ItemWithDateDto> findByOwnerId(long ownerId);

    List<ItemDto> searchByText(String text);
}
