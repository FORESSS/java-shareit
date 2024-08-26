package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.comment.dto.CommentDTO;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.Validator;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final Validator validator;

    @Override
    public Collection<ItemDTO> getAllItems(long userId) {
        validator.checkUserId(userId);
        log.info("Получение списка всех предметов пользователя с id: {}", userId);
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(itemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDTO getItemById(long itemId) {
        Item item = validator.validateAndGetItem(itemId);
        ItemDTO itemDto = itemMapper.toItemDto(item);
        itemDto.setComments(commentRepository.findAllByItemId(itemId).stream()
                .map(commentMapper::toCommentDto)
                .toList());
        log.info("Получение предмета с id: {}", itemId);
        return itemDto;
    }

    @Override
    public Collection<ItemDTO> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        log.info("Поиск предметов по тексту: {}", text);
        return itemRepository.searchByNameOrDescription(text)
                .stream()
                .map(itemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDTO createItem(long userId, ItemDTO itemDto) {
        User user = validator.validateAndGetUser(userId);
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(user);
        itemRepository.save(item);
        log.info("Создание нового предмета с id: {} пользователя с id: {}", item.getId(), userId);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDTO updateItem(long userId, long itemId, ItemDTO newItemDto) {
        Item item = validator.validateAndGetItem(itemId);
        validator.checkItemOwner(userId, item);
        Optional.ofNullable(newItemDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(newItemDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(newItemDto.getAvailable()).ifPresent(item::setAvailable);
        itemRepository.save(item);
        log.info("Обновление предмета с id: {} пользователя с id: {}", itemId, userId);
        return itemMapper.toItemDto(item);
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        validator.checkUserId(userId);
        validator.checkItemId(itemId);
        log.info("Удаление предмета с id: {} пользователя с id: {}", itemId, userId);
        itemRepository.deleteById(itemId);
    }

    @Override
    public CommentDTO addComment(long userId, long itemId, Comment comment) {
        User author = validator.validateAndGetUser(userId);
        Item item = validator.validateAndGetItem(itemId);
        validator.validateUserHasPastBooking(userId);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setAuthor(author);
        commentRepository.save(comment);
        log.info("Добавлен комментарий к предмету с id: {} пользователя с id: {}", itemId, userId);
        return commentMapper.toCommentDto(comment);
    }
}