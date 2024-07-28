package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {

    Collection<Item> getAllItems(long userId);

    Optional<Item> getItemById(long itemId);

    Collection<Item> searchItems(String text);

    Item createItem(Item item);

    Item updateItem(long itemId, Item item);

    void deleteItem(long itemId);

    boolean existsById(Long itemId);
}