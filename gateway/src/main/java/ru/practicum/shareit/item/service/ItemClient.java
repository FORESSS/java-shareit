package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> findByOwnerId(long userId) {
        return get("", userId);
    }


    public ResponseEntity<Object> searchByText(long userId, String text) {
        Map<String, Object> parameters = Map.of("text", text);
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> findById(long itemId) {
        return get("/" + itemId);
    }

    public ResponseEntity<Object> createItem(long userId, ItemDto item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> createComment(long userId, long itemId, CommentDto comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }

    public ResponseEntity<Object> update(long userId, long itemId, ItemDto newItem) {
        return patch("/" + itemId, userId, newItem);
    }
}