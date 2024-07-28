package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class ItemUpdateDTO {
    private long id;
    @Length(min = 1, max = 50)
    private String name;
    @Length(min = 1, max = 200)
    private String description;
    private Boolean available;
    private long owner;
    private long request;
}