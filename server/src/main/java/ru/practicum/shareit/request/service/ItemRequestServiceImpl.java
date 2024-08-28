package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.util.Validator;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final Validator validator;

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto getRequestById(long requestId) {
        log.info("Получение запроса вещи с id: {}", requestId);
        return mapRequestToDtoWithItems(validator.validateAndGetRequest(requestId));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemRequestDto> getRequestsByUserId(long userId) {
        validator.checkUserId(userId);
        log.info("Получение запросов пользователя с id: {}", userId);
        return mapRequestsToDtoWithItems(itemRequestRepository.findByOwnerId(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemRequestDto> getAllRequests(long userId) {
        validator.checkUserId(userId);
        log.info("Получение всех запросов для пользователя с id: {}", userId);
        return mapRequestsToDtoWithItems(itemRequestRepository.findByOwnerIdNotEquals(userId));
    }

    @Override
    public ItemRequestDto createRequest(long userId, ItemRequestDto request) {
        ItemRequest itemRequest = itemRequestMapper.itemRequestDtoToItemRequest(request);
        itemRequest.setOwner(validator.validateAndGetUser(userId));
        itemRequest.setCreated(LocalDateTime.now());
        log.info("Создание нового запроса от пользователя с id: {}", userId);
        return itemRequestMapper.itemRequestToItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    private ItemRequestDto mapRequestToDtoWithItems(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = itemRequestMapper.itemRequestToItemRequestDto(itemRequest);
        itemRequestDto.setItems(itemRepository.findByRequestId(itemRequest.getId()).stream()
                .map(itemMapper::itemToItemDto)
                .collect(Collectors.toList()));
        return itemRequestDto;
    }

    private Collection<ItemRequestDto> mapRequestsToDtoWithItems(Collection<ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(this::mapRequestToDtoWithItems)
                .collect(Collectors.toList());
    }
}