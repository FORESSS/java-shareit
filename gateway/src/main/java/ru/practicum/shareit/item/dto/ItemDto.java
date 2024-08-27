package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;

import java.util.List;

@Data
@Builder
public class ItemDto {
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private long ownerId;
    private List<CommentDto> comments;
    private ResponseBookingDto lastBooking;
    private ResponseBookingDto nextBooking;
    private long requestId;
}
