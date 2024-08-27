package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDateDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.Validator;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;
    private final ItemRequestRepository itemRequestRepository;
    private final Validator validator;

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemWithDateDto> getItemsByOwnerId(long ownerId) {
        validator.checkUserId(ownerId);
        Collection<Item> items = itemRepository.findByOwnerId(ownerId);
        List<ItemWithDateDto> itemWithDateDto = items.stream()
                .map(itemMapper::itemToItemWithDateDto)
                .toList();
        for (ItemWithDateDto item : itemWithDateDto) {
            Booking booking = bookingRepository.findByItemId(item.getId());
            List<CommentDto> comment = commentRepository.findByItemId(item.getId()).stream()
                    .map(commentMapper::commentToCommentDto).toList();
            if (booking != null) {
                item.setStart(booking.getStart());
                item.setEnd(booking.getEnd());
                item.setComments(comment);
            }
        }
        log.info("Получение списка всех предметов пользователя с id: {}", ownerId);
        return itemWithDateDto;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItemById(long itemId) {
        Item item = validator.validateAndGetItem(itemId);
        List<CommentDto> comment = commentRepository.findByItemId(itemId).stream()
                .map(commentMapper::commentToCommentDto)
                .toList();
        ItemDto itemDto = itemMapper.itemToItemDto(item);
        itemDto.setComments(comment);
        itemDto.setLastBooking(bookingMapper
                .bookingToResponseBookingDto(bookingRepository
                        .findByItemIdPast(itemId, LocalDateTime.now(), Status.REJECTED)));
        itemDto.setNextBooking(bookingMapper
                .bookingToResponseBookingDto(bookingRepository
                        .findByItemIdFuture(itemId, LocalDateTime.now(), Status.REJECTED)));
        log.info("Получение предмета с id: {}", itemId);
        return itemDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemDto> searchByText(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        Collection<Item> items = itemRepository.findBySearch(text.toLowerCase());
        log.info("Поиск предметов по тексту: {}", text);
        return items.stream()
                .map(itemMapper::itemToItemDto)
                .toList();
    }

    @Override
    public ItemDto createItem(long ownerId, ItemDto itemDto) {
        User user = validator.validateAndGetUser(ownerId);
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() > 0) {
            itemRequest = validator.validateAndGetRequest(itemDto.getRequestId());
        }
        Item item = itemMapper.itemDtoToItem(itemDto);
        item.setOwner(user);
        item.setRequest(itemRequest);
        item = itemRepository.save(item);
        log.info("Создание нового предмета с id: {} пользователя с id: {}", item.getId(), ownerId);
        return itemMapper.itemToItemDto(item);
    }

    @Override
    public CommentDto createComment(long userId, long itemId, CommentDto newComment) {
        User user = validator.validateAndGetUser(userId);
        Item item = validator.validateAndGetItem(itemId);
        if (bookingRepository.findByUserId(userId, LocalDateTime.now()).isEmpty()) {
            System.out.println(LocalDateTime.now());
            throw new ValidationException("Данный пользователь не использовал эту вещь, время : " + LocalDateTime.now());
        }
        Comment comment = commentMapper.commentDtoToComment(newComment);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        comment = commentRepository.save(comment);
        log.info("Добавлен комментарий к предмету с id: {} пользователя с id: {}", itemId, userId);
        return commentMapper.commentToCommentDto(comment);
    }

    @Override
    public ItemDto updateItem(long ownerId, long itemId, ItemDto newItem) {
        validator.checkUserId(ownerId);
        Item item = validator.validateAndGetItem(itemId);
       // validator.checkItemOwner(ownerId, item);
        if (newItem.getName() != null && !newItem.getName().isBlank()) {
            item.setName(newItem.getName());
        }
        if (newItem.getDescription() != null && !newItem.getDescription().isBlank()) {
            item.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            item.setAvailable(newItem.getAvailable());
        }
        log.info("Обновление предмета с id: {} пользователя с id: {}", itemId, ownerId);
        return itemMapper.itemToItemDto(item);
    }
}