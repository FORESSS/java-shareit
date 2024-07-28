package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private long currentId = 0;
    Map<Long, Item> items = new HashMap<>();

    @Override
    public Collection<Item> getAllItems(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> getItemById(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Collection<Item> searchItems(String text) {
        String textLowerCase = text.toLowerCase();
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(textLowerCase) ||
                        item.getDescription().toLowerCase().contains(textLowerCase)) &&
                        item.isAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public Item createItem(Item item) {
        item.setId(generateNewId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(long itemId, Item item) {
        items.put(itemId, item);
        return item;
    }

    @Override
    public void deleteItem(long itemId) {
        items.remove(itemId);
    }

    @Override
    public boolean existsById(Long itemId) {
        return items.containsKey(itemId);
    }

    private long generateNewId() {
        return ++currentId;
    }
}