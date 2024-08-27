package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemWithDateDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long ownerId;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<CommentDto> comments;
    private long requestId;
}