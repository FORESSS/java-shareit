package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDateDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = {"spring.datasource.driver-class-name=org.h2.Driver",
                "spring.datasource.url=jdbc:h2:mem:shareit",
                "spring.datasource.username=fores",
                "spring.datasource.password=12345"},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ItemServiceImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private BookingService bookingService;
    private User user;
    private User ownerItemRequest;
    private ItemDto item;
    private ItemRequest itemRequest;

    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setName("User");
        user.setEmail("test@test.com");

        ownerItemRequest = new User();
        ownerItemRequest.setName("Owner");
        ownerItemRequest.setEmail("owner@test.com");

        itemRequest = new ItemRequest();
        itemRequest.setDescription("Description");
        itemRequest.setOwner(ownerItemRequest);
        itemRequest.setCreated(LocalDateTime.now());

        item = ItemDto.builder()
                .name("Item")
                .description("Item")
                .available(true)
                .build();
    }

    @Test
    void testGetItemById() {
        saveEntity();
        item.setRequestId(itemRequest.getId());
        ItemDto itemDto = itemService.createItem(user.getId(), item);
        itemDto = itemService.getItemById(itemDto.getId());
        assertThat(itemDto.getId(), notNullValue());
        assertThat(itemDto.getName(), equalTo(item.getName()));
        assertThat(itemDto.getDescription(), equalTo(item.getDescription()));
        assertThat(itemDto.getAvailable(), equalTo(item.getAvailable()));
        assertThat(itemDto.getRequestId(), equalTo(itemRequest.getId()));
    }

    @Test
    void testGetItemsByOwnerId() {
        saveEntity();
        item.setRequestId(itemRequest.getId());
        for (int i = 0; i < 5; i++) {
            itemService.createItem(user.getId(), item);
        }
        Collection<ItemWithDateDto> items = itemService.getItemsByOwnerId(user.getId());
        assertThat(items.size(), equalTo(5));
    }

    @Test
    void testSearchByText() {
        saveEntity();
        item.setRequestId(itemRequest.getId());
        itemService.createItem(user.getId(), item);
        Collection<ItemDto> items = itemService.searchByText("Item");
       assertThat(items.size(), equalTo(1));
        assertThat(items.stream().toList().get(0).getName(), equalTo("Item"));
    }

    @Test
    void testCreateItem() {
        saveEntity();
        item.setRequestId(itemRequest.getId());
        ItemDto itemDto = itemService.createItem(user.getId(), item);
        assertThat(itemDto.getId(), notNullValue());
        assertThat(itemDto.getName(), equalTo(item.getName()));
        assertThat(itemDto.getDescription(), equalTo(item.getDescription()));
        assertThat(itemDto.getAvailable(), equalTo(item.getAvailable()));
        assertThat(itemDto.getRequestId(), equalTo(itemRequest.getId()));
    }

    @Test
    void testCreateComment() {
        saveEntity();
        ItemDto itemDto = itemService.createItem(user.getId(), item);
        RequestBookingDto booking = RequestBookingDto.builder()
                .start(LocalDateTime.now().minusHours(2))
                .end(LocalDateTime.now().minusHours(2))
                .itemId(itemDto.getId())
                .build();
        bookingService.createBooking(user.getId(), booking);
        CommentDto comment = CommentDto.builder()
                .text("text")
                .build();
        CommentDto commentDto = itemService.createComment(user.getId(), itemDto.getId(), comment);
        assertThat(commentDto.getId(), notNullValue());
        assertThat(commentDto.getText(), equalTo(comment.getText()));
        assertThat(commentDto.getAuthorName(), equalTo(user.getName()));
        assertThat(commentDto.getItemId(), equalTo(itemDto.getId()));
    }

    @Test
    void testUpdateItem() {
        saveEntity();
        item.setRequestId(itemRequest.getId());
        ItemDto itemDto = itemService.createItem(user.getId(), item);
        item.setName("Item2");
        ItemDto updateItemDto = itemService.updateItem(user.getId(), itemDto.getId(), item);
        assertThat(updateItemDto.getId(), notNullValue());
        assertThat(updateItemDto.getName(), equalTo(item.getName()));
        assertThat(updateItemDto.getDescription(), equalTo(item.getDescription()));
        assertThat(updateItemDto.getAvailable(), equalTo(item.getAvailable()));
        assertThat(updateItemDto.getRequestId(), equalTo(itemRequest.getId()));
    }

    private void saveEntity() {
        user = userRepository.save(user);
        ownerItemRequest = userRepository.save(ownerItemRequest);
        itemRequest = itemRequestRepository.save(itemRequest);
    }
}