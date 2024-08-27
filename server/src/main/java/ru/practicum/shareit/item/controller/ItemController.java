package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDateDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

import static ru.practicum.shareit.util.Constants.USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public Collection<ItemWithDateDto> getItemsByOwnerId(@RequestHeader(USER_ID) long ownerId) {
        return itemService.getItemsByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchByText(@RequestParam(defaultValue = "") String text) {
        return itemService.searchByText(text);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(USER_ID) long ownerId, @RequestBody ItemDto item) {
        return itemService.createItem(ownerId, item);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(USER_ID) long userId, @PathVariable long itemId,
                                    @RequestBody CommentDto comment) {
        return itemService.createComment(userId, itemId, comment);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_ID) long ownerId, @PathVariable long itemId,
                              @RequestBody ItemDto newItem) {
        return itemService.updateItem(ownerId, itemId, newItem);
    }
}