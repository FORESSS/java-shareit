package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {
    WAITING(1),
    APPROVED(2),
    REJECTED(3),
    CANCELED(4);

    private final int id;
}