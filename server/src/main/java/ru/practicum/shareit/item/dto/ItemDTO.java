package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;

import java.util.List;

@Data
@Builder
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long ownerId;
    private List<CommentDto> comments;
    private ResponseBookingDto lastBooking;
    private ResponseBookingDto nextBooking;
    private long requestId;
}
