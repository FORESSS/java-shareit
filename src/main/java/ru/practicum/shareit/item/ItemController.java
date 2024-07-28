package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDTO> getAllItems(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
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
    public ItemDTO createItem(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                              @RequestBody @Valid ItemDTO itemDto) {
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO updateItem(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                              @PathVariable @Positive long itemId,
                              @RequestBody @Valid ItemUpdateDTO itemUpdateDTO) {
        return itemService.updateItem(userId, itemId, itemUpdateDTO);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                           @PathVariable @Positive long itemId) {
        itemService.deleteItem(userId, itemId);
    }
}