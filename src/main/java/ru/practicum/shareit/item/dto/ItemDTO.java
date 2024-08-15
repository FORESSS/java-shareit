package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.comment.dto.CommentDTO;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
public class ItemDTO {
    private long id;
    @NotBlank
    @Length(max = 50)
    private String name;
    @NotBlank
    @Length(max = 200)
    private String description;
    @NotNull
    private Boolean available;
    private ItemRequest request;
    private Collection<CommentDTO> comments;
    private BookingDTO lastBooking;
    private BookingDTO nextBooking;
}