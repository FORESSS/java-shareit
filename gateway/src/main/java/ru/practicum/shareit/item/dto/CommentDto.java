package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import static ru.practicum.shareit.constants.Constants.PATTERN;

@Data
@Builder
public class CommentDto {
    private long id;
    @NotBlank
    private String text;
    private long itemId;
    private String authorName;
    @JsonFormat(pattern = PATTERN)
    private LocalDateTime created;
}
