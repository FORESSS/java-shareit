package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

public class ItemClient extends BaseClient {

    @Autowired
    public ItemClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> getItemById(long itemId) {
        return get("/" + itemId);
    }

    public ResponseEntity<Object> getItemsByOwnerId(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> searchItemByText(long userId, String text) {
        Map<String, Object> parameters = Map.of("text", text);
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> createItem(long userId, ItemDto item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> createComment(long userId, long itemId, CommentDto comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }

    public ResponseEntity<Object> updateItem(long userId, long itemId, ItemDto newItem) {
        return patch("/" + itemId, userId, newItem);
    }
}