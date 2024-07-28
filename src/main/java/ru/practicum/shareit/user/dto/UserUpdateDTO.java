package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class UserUpdateDTO {
    @Length(min = 1, max = 50)
    private String name;
    @Email
    private String email;
}