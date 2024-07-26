package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class ItemDto {
    @NotNull
    private long id;
    @NotBlank
    @Length(max = 50)
    private String name;
    @NotBlank
    @Length(max = 200)
    private String description;
    @NotNull
    @AssertTrue
    private Boolean available;
    @NotNull
    private long owner;
    @NotNull
    private long request;
}