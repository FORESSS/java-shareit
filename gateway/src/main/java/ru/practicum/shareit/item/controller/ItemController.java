package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemClient;

import static ru.practicum.shareit.constants.Constants.USER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("items")
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemDto item, @RequestHeader(USER_ID) long ownerId) {
        log.info("Получен POST запрос на создание предмета {} пользователем с ownerId = {}", item, ownerId);
        return itemClient.createItem(ownerId, item);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto comment,
                                                @PathVariable long itemId,
                                                @RequestHeader(USER_ID) long userId) {
        log.info("Получен POST запрос на создание комментария {} на предмет с itemId = {} от пользователя " +
                "с userId = {}", comment, itemId, userId);
        return itemClient.createComment(userId, itemId, comment);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody ItemDto newItem,
                                         @PathVariable long itemId,
                                         @RequestHeader(USER_ID) long ownerId) {
        log.info("Получен PATCH запрос на обновление предмета с itemId = {} от пользователя с ownerId = {}, " +
                "поля, которые нужно обновить: {}", itemId, ownerId, newItem);
        return itemClient.update(ownerId, itemId, newItem);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(@PathVariable long itemId) {
        log.info("Получен GET запрос на получение предмета с itemId = {}", itemId);
        return itemClient.findById(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> findByOwnerId(@RequestHeader(USER_ID) long ownerId) {
        log.info("Получен GET запрос на получение всех предметов пользователя с ownerId = {}", ownerId);
        return itemClient.findByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchByText(@RequestHeader(USER_ID) long userId,
                                               @RequestParam(defaultValue = "") String text) {
        log.info("Получен GET запрос на получение предмета по поиску text = {}", text);
        return itemClient.searchByText(userId, text);
    }
}