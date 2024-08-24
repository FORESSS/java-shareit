/*package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDTO;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDTO> getAllItems(@RequestHeader(USER_ID_HEADER) @Positive long userId) {
        return itemService.getAllItems(userId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO getItemById(@PathVariable @NonNull Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDTO> searchItems(@RequestParam("text") String text) {
        return itemService.searchItems(text);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDTO createItem(@RequestHeader(USER_ID_HEADER) @Positive long userId,
                              @RequestBody @Valid ItemDTO itemDto) {
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO updateItem(@RequestHeader(USER_ID_HEADER) long userId,
                              @PathVariable long itemId,
                              @RequestBody ItemDTO itemDTO) {
        return itemService.updateItem(userId, itemId, itemDTO);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItem(@RequestHeader(USER_ID_HEADER) @Positive long userId,
                           @PathVariable @Positive long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDTO addComment(@PathVariable long itemId,
                                 @RequestHeader(USER_ID_HEADER) long userId,
                                 @Valid @RequestBody Comment comment) {
        return itemService.addComment(userId, itemId, comment);
    }
}*/