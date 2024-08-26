package ru.practicum.shareit.request.RequestService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.Validator;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final Validator validator;

    @Override
    public Collection<ItemRequestDTO> getItemRequestsByUserId(long userId) {
        validator.checkUserId(userId);
        Collection<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(userId);
        log.info("Получение запросов пользователя с id: {}", userId);
        return itemRequests.stream()
                .map(request -> {
                    ItemRequestDTO itemRequestDto = itemRequestMapper.toItemRequestDto(request);
                    itemRequestDto.setItems(itemRepository.findAllByRequestId(request.getId()).stream()
                            .map(itemMapper::toItemDto)
                            .collect(Collectors.toList()));
                    return itemRequestDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemRequestDTO> getAllItemRequests(long userId) {
        validator.checkUserId(userId);
        Collection<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterIdNotOrderByCreatedDesc(userId);
        log.info("Получение всех запросов других пользователей для пользователя с id: {}", userId);
        return itemRequests.stream()
                .map(request -> {
                    ItemRequestDTO itemRequestDto = itemRequestMapper.toItemRequestDto(request);
                    itemRequestDto.setItems(itemRepository.findAllByRequestId(request.getId()).stream()
                            .map(itemMapper::toItemDto)
                            .collect(Collectors.toList()));
                    return itemRequestDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDTO getItemRequestById(long userId, long requestId) {
        validator.checkUserId(userId);
        ItemRequest itemRequest = validator.validateAndGetItemRequest(requestId);
        ItemRequestDTO itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(itemRepository.findAllByRequestId(requestId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList()));
        log.info("Получен запрос с id: {} пользователя с id: {}", requestId, userId);
        return itemRequestDto;
    }

    @Override
    public ItemRequestDTO createItemRequest(long userId, ItemRequestDTO requestDto) {
        User requester = validator.validateAndGetUser(userId);
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(requestDto);
        itemRequest.setRequester(requester);
        itemRequest.setCreated(LocalDateTime.now());
        ItemRequest savedItemRequest = itemRequestRepository.save(itemRequest);
        log.info("Добавлен запрос с id: {} пользователя с id: {}", savedItemRequest.getId(), userId);
        return itemRequestMapper.toItemRequestDto(savedItemRequest);
    }
}