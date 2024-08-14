package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDTOExport;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.InvalidCommentException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.Validator;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final Validator validator;

    @Override
    public Collection<ItemDTO> getAllItems(long userId) {
        validator.checkUserId(userId);
        log.info("Возврат списка всех предметов");
        return itemRepository.findAll().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDTO getItemById(long itemId) {
        List<CommentDTOExport> comments = commentRepository.findAllByItemId(itemId)
                .stream()
                .map(CommentMapper::toCommentDtoExport)
                .toList();
        ItemDTO itemDto = ItemMapper.toItemDto(itemRepository.findById(itemId).get());
        itemDto.setComments(comments);
        return itemDto;
    }

    @Override
    public Collection<ItemDTO> searchItems(String text) {
        if (isNull(text) || text.isBlank()) {
            return Collections.emptyList();
        }
        log.info("Поиск предметов по тексту: {}", text);
        return itemRepository.findByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDTO createItem(long userId, ItemDTO itemDto) {
        validator.checkUserId(userId);
        Item item = ItemMapper.toItem(itemDto);
        User user = userRepository.findById(userId).orElseThrow();
        item.setOwner(user);
        log.info("Создание нового предмета для пользователя с id: {}", userId);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDTO updateItem(long userId, long itemId, ItemDTO newItemDto) {
        validator.checkItemId(itemId);
        Item oldItem = itemRepository.findById(itemId).get();

        if (oldItem.getOwner().getId() != userId) {
            throw new ValidationException("Only owner can update item");
        }

        if (newItemDto.getName() != null) {
            oldItem.setName(newItemDto.getName());
        }
        if (newItemDto.getDescription() != null) {
            oldItem.setDescription(newItemDto.getDescription());
        }
        if (newItemDto.getAvailable() != null) {
            oldItem.setAvailable(newItemDto.getAvailable());
        }

        Item item = itemRepository.save(oldItem);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        validator.checkUserId(userId);
        validator.checkItemId(itemId);
        log.info("Удаление предмета с id: {} для пользователя с id: {}", itemId, userId);
        itemRepository.deleteById(itemId);
    }

    @Override
    public CommentDTOExport addComment(long userId, long itemId, Comment comment) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidCommentException(String.format("Can not find user with id %d.", userId)));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new InvalidCommentException(String.format("Can not find item with id %d.", itemId)));

        Optional<Booking> bookingOptional = bookingRepository.findFirstByBookerIdAndEndBeforeAndStatusNot(userId, LocalDateTime.now(), BookingStatus.REJECTED);
        if (bookingOptional.isEmpty()) {
            throw new AccessException("Can not add comment, because there was no booking.");
        }
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setAuthor(author);

        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toCommentDtoExport(savedComment);

    }
}