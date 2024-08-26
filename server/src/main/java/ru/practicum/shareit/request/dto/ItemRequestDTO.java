package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
public class ItemRequestDTO {
    private long id;
    private String description;
    private User requester;
    private LocalDateTime created;
    private Collection<ItemDTO> items;
}