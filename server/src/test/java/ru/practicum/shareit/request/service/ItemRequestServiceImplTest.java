package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Transactional
@SpringBootTest(
        properties = {"spring.datasource.driver-class-name=org.h2.Driver",
                "spring.datasource.url=jdbc:h2:mem:shareit",
                "spring.datasource.username=dbuser",
                "spring.datasource.password=12345"},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
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
    void beforeEach() {
        user = new User();
        user.setName("Max");
        user.setEmail("max@ya.ru");

        user2 = new User();
        user2.setName("Max2");
        user2.setEmail("2@ya.ru");

        item = new Item();
        item.setOwner(user);
        item.setName("Дрель");
        item.setAvailable(true);
        item.setDescription("setDescription");

        itemRequest = ItemRequestDto.builder()
                .description("description")
                .build();
    }

    /*@Test
    void create() {
        saveEntity();

        ItemRequestDto itemRequestDto = itemRequestService.createRequest(itemRequest, user.getId());
        assertThat(itemRequestDto.getId(), notNullValue());
        assertThat(itemRequestDto.getDescription(), equalTo(itemRequest.getDescription()));
    }

    @Test
    void createFailUser() {
        saveEntity();

        assertThatThrownBy(() -> itemRequestService.createRequest(itemRequest, 3L));
    }

    @Test
    void findByUserId() {
        saveEntity();

        for (int i = 0; i < 5; i++) {
            itemRequestService.createRequest(itemRequest, user.getId());
        }

        List<ItemRequestDto> itemRequests = itemRequestService.getRequestsByUserId(user.getId());
        assertThat(itemRequests.size(), equalTo(5));
    }

    @Test
    void findAll() {
        saveEntity();

        for (int i = 0; i < 5; i++) {
            itemRequestService.createRequest(itemRequest, user2.getId());
        }

        List<ItemRequestDto> itemRequests = itemRequestService.getAllRequests(user.getId());
        assertThat(itemRequests.size(), equalTo(5));
    }

    @Test
    void findByRequestId() {
        saveEntity();
        ItemRequestDto itemRequestDto = itemRequestService.createRequest(itemRequest, user.getId());

        itemRequestDto = itemRequestService.getRequestById(itemRequestDto.getId());
        assertThat(itemRequestDto.getId(), notNullValue());
        assertThat(itemRequestDto.getDescription(), equalTo(itemRequest.getDescription()));
    }

    private void saveEntity() {
        user = userRepository.save(user);
        user2 = userRepository.save(user2);
        item = itemRepository.save(item);
    }*/
}