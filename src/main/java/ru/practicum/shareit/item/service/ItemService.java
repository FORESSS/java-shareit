package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;

import java.util.Collection;

public interface ItemService {

    Collection<ItemDTO> getAllItems(long userId);

    ItemDTO getItemById(long itemId);

    Collection<ItemDTO> searchItems(String text);

    ItemDTO createItem(long userId, ItemDTO item);

    ItemDTO updateItem(long userId, long itemId, ItemUpdateDTO item);

    void deleteItem(long userId, long itemId);
}