package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemClient;

import static ru.practicum.shareit.util.Constants.USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("items")
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemById(@PathVariable @Positive long itemId) {
        return itemClient.getItemById(itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemsByOwnerId(@RequestHeader(USER_ID) @Positive long ownerId) {
        return itemClient.getItemsByOwnerId(ownerId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> searchItemByText(@RequestHeader(USER_ID) @Positive long userId,
                                                   @RequestParam(defaultValue = "") String text) {
        return itemClient.searchItemByText(userId, text);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItem(@RequestHeader(USER_ID) @Positive long ownerId,
                                             @RequestBody @Valid ItemDto item) {
        return itemClient.createItem(ownerId, item);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createComment(@RequestHeader(USER_ID) @Positive long userId,
                                                @PathVariable @Positive long itemId,
                                                @RequestBody @Valid CommentDto comment) {
        return itemClient.createComment(userId, itemId, comment);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_ID) @Positive long ownerId,
                                             @PathVariable @Positive long itemId,
                                             @RequestBody @Valid ItemDto newItem) {
        return itemClient.updateItem(ownerId, itemId, newItem);
    }
}