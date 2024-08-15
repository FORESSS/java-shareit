package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class UserDTO {
    @NotNull
    private long id;
    @NotBlank
    @Length(max = 50)
    private String name;
    @NotBlank
    private String email;
}