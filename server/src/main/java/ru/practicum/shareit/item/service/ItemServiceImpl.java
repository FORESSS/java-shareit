package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.DataAccessException;
import ru.practicum.shareit.exception.NotFoundException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Override
    public ItemDto createItem(ItemDto itemDto, long ownerId) {
        log.info("Начало процесса создания предмета с ownerId = {}", ownerId);
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Такого владельца не существует"));
        ItemRequest itemRequest = null;

        if (itemDto.getRequestId() > 0) {
            itemRequest = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Такого владельца не существует"));
        }

        Item item = itemMapper.itemDtoToItem(itemDto);
        item.setOwner(user);
        item.setRequest(itemRequest);
        item = itemRepository.save(item);
        log.info("Предмет создан");
        return itemMapper.itemToItemDto(item);
    }

    @Override
    public CommentDto createComment(CommentDto newComment, long itemId, long userId) {
        log.info("Начало процесса создания комментария на предмет с itemId = {} от пользователя с userId = {}",
                itemId, userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Такого предмета не существует"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя не существует"));

        if (bookingRepository.findByUserId(userId, LocalDateTime.now()).isEmpty()) {
            System.out.println(LocalDateTime.now());
            throw new ValidationException("Данный пользователь не использовал эту вещь, время : " + LocalDateTime.now());
        }

        Comment comment = commentMapper.commentDtoToComment(newComment);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        comment = commentRepository.save(comment);
        log.info("Комментарий создан");
        return commentMapper.commentToCommentDto(comment);
    }

    @Override
    public ItemDto update(ItemDto newItem, long id, long ownerId) {
        log.info("Начало процесса обновления предмета с id = {} и ownerId = {}", id, ownerId);
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Такого предмета не существует"));

        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("Такого владельца не существует");
        }
        if (item.getOwner().getId() != ownerId) {
            throw new DataAccessException("У вас нет доступа к этой информации");
        }

        if (newItem.getName() != null && !newItem.getName().isBlank()) {
            item.setName(newItem.getName());
        }
        if (newItem.getDescription() != null && !newItem.getDescription().isBlank()) {
            item.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            item.setAvailable(newItem.getAvailable());
        }

        log.info("Предмет обновлен");
        return itemMapper.itemToItemDto(item);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDto findById(long id) {
        log.info("Начало процесса получения предмета по id = {}", id);
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Такого предмета не существует"));
        List<CommentDto> comment = commentRepository.findByItemId(id).stream()
                        .map(commentMapper::commentToCommentDto)
                        .toList();
        ItemDto itemDto = itemMapper.itemToItemDto(item);
        itemDto.setComments(comment);
        itemDto.setLastBooking(bookingMapper
                .bookingToResponseBookingDto(bookingRepository
                        .findByItemIdPast(id, LocalDateTime.now(), Status.REJECTED)));
        itemDto.setNextBooking(bookingMapper
                .bookingToResponseBookingDto(bookingRepository
                        .findByItemIdFuture(id, LocalDateTime.now(), Status.REJECTED)));
        log.info("Предмет получен");
        return itemDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemWithDateDto> findByOwnerId(long ownerId) {
        log.info("Начало процесса получения всех предметов пользователя с ownerId = {}", ownerId);

        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("Такого владельца не существует");
        }

        List<Item> items = itemRepository.findByOwnerId(ownerId);
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

        log.info("Список всех предметов получен");
        return itemWithDateDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> searchByText(String text) {
        log.info("Начало процесса получения предметов по поиску с text = {}", text);

        if (text.isBlank()) {
            log.info("Текст поиска пустой - список предметов пустой");
            return new ArrayList<>();
        }

        List<Item> items = itemRepository.findBySearch(text.toLowerCase());
        log.info("Список предметов по поиску получен");
        return items.stream()
                .map(itemMapper::itemToItemDto)
                .toList();
    }
}