package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = {
                "spring.datasource.driver-class-name=org.h2.Driver",
                "spring.datasource.url=jdbc:h2:mem:shareit",
                "spring.datasource.username=fores",
                "spring.datasource.password=12345"
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class ItemRequestServiceImplTest {
    @Autowired
    private ItemRequestService itemRequestService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    private User user;
    private User user2;
    private Item item;
    private ItemRequestDto itemRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("User");
        user.setEmail("test@test.com");

        user2 = new User();
        user2.setName("User2");
        user2.setEmail("test2@test.ru");

        item = new Item();
        item.setOwner(user);
        item.setName("Item");
        item.setAvailable(true);
        item.setDescription("Item");

        itemRequest = ItemRequestDto.builder()
                .description("Request")
                .build();
    }

    @Test
    void testGetRequestById() {
        saveEntities();
        ItemRequestDto itemRequestDto = itemRequestService.createRequest(user.getId(), itemRequest);
        itemRequestDto = itemRequestService.getRequestById(itemRequestDto.getId());
        assertThat(itemRequestDto.getId(), notNullValue());
        assertThat(itemRequestDto.getDescription(), equalTo(itemRequest.getDescription()));
    }

    @Test
    void testGetRequestsByUserId() {
        saveEntities();
        for (int i = 0; i < 5; i++) {
            itemRequestService.createRequest(user.getId(), itemRequest);
        }
        Collection<ItemRequestDto> itemRequests = itemRequestService.getRequestsByUserId(user.getId());
        assertThat(itemRequests.size(), equalTo(5));
    }

    @Test
    void testGetAllRequests() {
        saveEntities();
        for (int i = 0; i < 5; i++) {
            itemRequestService.createRequest(user2.getId(), itemRequest);
        }
        Collection<ItemRequestDto> itemRequests = itemRequestService.getAllRequests(user.getId());
        assertThat(itemRequests.size(), equalTo(5));
    }

    @Test
    void testCreate() {
        saveEntities();
        ItemRequestDto itemRequestDto = itemRequestService.createRequest(user.getId(), itemRequest);
        assertThat(itemRequestDto.getId(), notNullValue());
        assertThat(itemRequestDto.getDescription(), equalTo(itemRequest.getDescription()));
    }

    private void saveEntities() {
        user = userRepository.save(user);
        user2 = userRepository.save(user2);
        item = itemRepository.save(item);
    }
}