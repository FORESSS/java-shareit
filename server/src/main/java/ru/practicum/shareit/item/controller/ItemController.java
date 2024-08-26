package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDateDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.booking.controller.BookingController.USER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto item, @RequestHeader(USER_ID) long ownerId) {
        log.info("Получен POST запрос на создание предмета {} пользователем с ownerId = {}", item, ownerId);
        return itemService.createItem(item, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody CommentDto comment,
                                    @PathVariable long itemId,
                                    @RequestHeader(USER_ID) long userId) {
        log.info("Получен POST запрос на создание комментария {} на предмет с itemId = {} от пользователя " +
                "с userId = {}", comment, itemId, userId);
        return itemService.createComment(comment, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto newItem,
                          @PathVariable long itemId,
                          @RequestHeader(USER_ID) long ownerId) {
        log.info("Получен PATCH запрос на обновление предмета с itemId = {} от пользователя с ownerId = {}, " +
                "поля, которые нужно обновить: {}", itemId, ownerId, newItem);
        return itemService.update(newItem, itemId, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable long itemId) {
        log.info("Получен GET запрос на получение предмета с itemId = {}", itemId);
        return itemService.findById(itemId);
    }

    @GetMapping
    public List<ItemWithDateDto> findByOwnerId(@RequestHeader(USER_ID) long ownerId) {
        log.info("Получен GET запрос на получение всех предметов пользователя с ownerId = {}", ownerId);
        return itemService.findByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchByText(@RequestParam(defaultValue = "") String text) {
        log.info("Получен GET запрос на получение предмета по поиску text = {}", text);
        return itemService.searchByText(text);
    }
}