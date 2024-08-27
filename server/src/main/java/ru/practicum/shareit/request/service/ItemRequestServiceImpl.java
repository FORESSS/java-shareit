package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.Validator;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final Validator validator;

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto getRequestById(long requestId) {
        ItemRequest itemRequest = validator.validateAndGetRequest(requestId);
        ItemRequestDto itemRequestDto = itemRequestMapper.itemRequestToItemRequestDto(itemRequest);
        List<ItemDto> items = itemRepository.findByRequestId(requestId).stream()
                .map(itemMapper::itemToItemDto)
                .toList();
        itemRequestDto.setItems(items);
        log.info("");
        return itemRequestDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemRequestDto> getRequestsByUserId(long userId) {
        validator.checkUserId(userId);
        Collection<ItemRequest> itemRequests = itemRequestRepository.findByOwnerId(userId);
        List<ItemRequestDto> itemRequestsDto = itemRequests.stream()
                .map(itemRequestMapper::itemRequestToItemRequestDto)
                .toList();
        log.info("");
        return getListRequestsDtoWithItems(itemRequestsDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemRequestDto> getAllRequests(long userId) {
        validator.checkUserId(userId);
        Collection<ItemRequest> itemRequests = itemRequestRepository.findByOwnerIdNotEquals(userId);
        List<ItemRequestDto> itemRequestsDto = itemRequests.stream()
                .map(itemRequestMapper::itemRequestToItemRequestDto)
                .toList();
        log.info("");
        return getListRequestsDtoWithItems(itemRequestsDto);
    }

    @Override
    public ItemRequestDto createRequest(long userId, ItemRequestDto request) {
        User user = validator.validateAndGetUser(userId);
        ItemRequest itemRequest = itemRequestMapper.itemRequestDtoToItemRequest(request);
        itemRequest.setOwner(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest = itemRequestRepository.save(itemRequest);
        log.info("");
        return itemRequestMapper.itemRequestToItemRequestDto(itemRequest);
    }

    private List<ItemRequestDto> getListRequestsDtoWithItems(List<ItemRequestDto> itemRequestsDto) {
        for (ItemRequestDto itemRequest : itemRequestsDto) {
            List<ItemDto> itemsDto = itemRepository.findByRequestId(itemRequest.getId()).stream()
                    .map(itemMapper::itemToItemDto)
                    .toList();
            itemRequest.setItems(itemsDto);
        }
        return itemRequestsDto;
    }
}