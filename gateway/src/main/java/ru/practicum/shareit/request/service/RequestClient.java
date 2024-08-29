package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

public class RequestClient extends BaseClient {

    @Autowired
    public RequestClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> getRequestById(long requestId) {
        return get("/" + requestId);
    }

    public ResponseEntity<Object> getRequestsByUserId(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getAllRequests(long userId) {
        return post("/all", userId);
    }

    public ResponseEntity<Object> createRequest(long userId, ItemRequestDto request) {
        return post("", userId, request);
    }
}