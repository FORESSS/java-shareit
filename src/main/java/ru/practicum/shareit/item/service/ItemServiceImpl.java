package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.util.Validator;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final Validator validator;

    @Override
    public Collection<ItemDTO> getAllItems(long userId) {
        validator.checkUserId(userId);
        log.info("Возврат списка всех предметов");
        return itemRepository.getAllItems(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDTO getItemById(long itemId) {
        log.info("Возврат предмета с id: {}", itemId);
        return itemRepository.getItemById(itemId)
                .map(ItemMapper::toItemDto)
                .orElseThrow(() -> new ValidationException("Предмет с id: " + itemId + " не найден"));
    }

    @Override
    public Collection<ItemDTO> searchItems(String text) {
        if (isNull(text) || text.isBlank()) {
            return Collections.emptyList();
        }
        log.info("Поиск предметов по тексту: {}", text);
        return itemRepository.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDTO createItem(long userId, ItemDTO itemDto) {
        validator.checkUserId(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userId);
        log.info("Создание нового предмета для пользователя с id: {}", userId);
        return ItemMapper.toItemDto(itemRepository.createItem(item));
    }

    @Override
    public ItemDTO updateItem(long userId, long itemId, ItemUpdateDTO itemUpdateDTO) {
        validator.checkUserId(userId);
        Item item = itemRepository.getItemById(itemId).orElseThrow(() ->
                new RuntimeException("Предмет с id: " + itemId + " не найден"));
        validator.checkOwner(userId, item);
        if (itemUpdateDTO.getName() != null) {
            item.setName(itemUpdateDTO.getName());
        }
        if (itemUpdateDTO.getDescription() != null) {
            item.setDescription(itemUpdateDTO.getDescription());
        }
        if (itemUpdateDTO.getAvailable() != null) {
            item.setAvailable(itemUpdateDTO.getAvailable());
        }
        item.setOwner(userId);
        log.info("Обновление предмета с id: {} для пользователя с id: {}", itemId, userId);
        return ItemMapper.toItemDto(itemRepository.updateItem(itemId, item));
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        validator.checkUserId(userId);
        validator.checkItemId(itemId);
        log.info("Удаление предмета с id: {} для пользователя с id: {}", itemId, userId);
        itemRepository.deleteItem(itemId);
    }
}