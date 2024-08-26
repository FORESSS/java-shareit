package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDTO;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDTO;

import java.util.Collection;

public interface ItemService {

    Collection<ItemDTO> getAllItems(long userId);

    ItemDTO getItemById(long itemId);

    Collection<ItemDTO> searchItems(String text);

    ItemDTO createItem(long userId, ItemDTO item);

    ItemDTO updateItem(long userId, long itemId, ItemDTO item);

    void deleteItem(long userId, long itemId);

    CommentDTO addComment(long userId, long itemId, Comment comment);
}