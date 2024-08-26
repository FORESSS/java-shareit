package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.comment.dto.CommentDTO;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

@Data
@Builder
public class ItemDTO {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
    private Collection<CommentDTO> comments;
    private BookingDTO lastBooking;
    private BookingDTO nextBooking;
}