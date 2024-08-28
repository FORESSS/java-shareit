package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDateDto;

import java.util.Collection;

public interface ItemService {
    ItemDto getItemById(long itemId);

    Collection<ItemWithDateDto> getItemsByOwnerId(long ownerId);

    Collection<ItemDto> searchByText(String text);

    ItemDto createItem(long userId, ItemDto item);

    CommentDto createComment(long userId, long itemId, CommentDto comment);

    ItemDto updateItem(long ownerId, long itemId, ItemDto itemDto);
}