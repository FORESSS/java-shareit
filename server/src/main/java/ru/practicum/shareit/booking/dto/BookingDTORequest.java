package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingDTORequest {
    private long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}