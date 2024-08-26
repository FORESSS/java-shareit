package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.constants.Constants.PATTERN;

@Data
@Builder
public class ItemRequestDto {
    private long id;
    @NotBlank
    private String description;
    @JsonFormat(pattern = PATTERN)
    private LocalDateTime created;
    private List<ItemDto> items;
}
