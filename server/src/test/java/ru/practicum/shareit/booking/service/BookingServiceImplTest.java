package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

@Transactional
@SpringBootTest(
        properties = {"spring.datasource.driver-class-name=org.h2.Driver",
                "spring.datasource.url=jdbc:h2:mem:shareit",
                "spring.datasource.username=dbuser",
                "spring.datasource.password=12345"},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BookingServiceImplTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingService bookingService;

    private User user;

    private Item item;

    private User ownerUser;

    private RequestBookingDto requestBookingDto;

    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setName("Max");
        user.setEmail("max@ya.ru");

        ownerUser = new User();
        ownerUser.setName("qwer");
        ownerUser.setEmail("qwer@ya.ru");

        item = new Item();
        item.setOwner(ownerUser);
        item.setName("Дрель");
        item.setAvailable(true);
        item.setDescription("setDescription");

        requestBookingDto = RequestBookingDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .itemId(1L)
                .build();
    }

   /* @Test
    void create() {
        saveEntity();
        requestBookingDto.setItemId(item.getId());

        ResponseBookingDto responseBookingDto = bookingService.createBooking(requestBookingDto, user.getId());
        assertThat(responseBookingDto.getId(), notNullValue());
        assertThat(responseBookingDto.getItem().getName(), equalTo(item.getName()));
        assertThat(responseBookingDto.getBooker().getName(), equalTo(user.getName()));
        assertThat((responseBookingDto.getStatus()), equalTo(Status.WAITING));
    }

    @Test
    void createFailUser() {
        saveEntity();

        assertThatThrownBy(() -> bookingService.createBooking(requestBookingDto, 3L));
    }

    @Test
    void createFailItem() {
        saveEntity();

        requestBookingDto.setItemId(5L);
        assertThatThrownBy(() -> bookingService.createBooking(requestBookingDto, 1L));
    }

    @Test
    void update() {
        saveEntity();
        requestBookingDto.setItemId(item.getId());

        ResponseBookingDto responseBookingDto = bookingService.createBooking(requestBookingDto, user.getId());

        responseBookingDto = bookingService.updateBooking(responseBookingDto.getId(), ownerUser.getId(), true);
        assertThat(responseBookingDto.getId(), notNullValue());
        assertThat(responseBookingDto.getItem().getName(), equalTo(item.getName()));
        assertThat(responseBookingDto.getBooker().getName(), equalTo(user.getName()));
        assertThat((responseBookingDto.getStatus()), equalTo(Status.APPROVED));
    }

    @Test
    void updateFailBooking() {
        saveEntity();
        requestBookingDto.setItemId(item.getId());
        bookingService.createBooking(requestBookingDto, user.getId());

        assertThatThrownBy(() -> bookingService.updateBooking(5L, ownerUser.getId(), true));
    }

    @Test
    void updateFailAccess() {
        saveEntity();
        requestBookingDto.setItemId(item.getId());
        ResponseBookingDto responseBookingDto = bookingService.createBooking(requestBookingDto, user.getId());

        assertThatThrownBy(() -> bookingService.updateBooking(responseBookingDto.getId(), 1L, true));
    }

    @Test
    void findById() {
        saveEntity();
        requestBookingDto.setItemId(item.getId());

        ResponseBookingDto responseBookingDto = bookingService.createBooking(requestBookingDto, user.getId());

        responseBookingDto = bookingService.getBookingById(responseBookingDto.getId(), user.getId());
        assertThat(responseBookingDto.getId(), notNullValue());
        assertThat(responseBookingDto.getItem().getName(), equalTo(item.getName()));
        assertThat(responseBookingDto.getBooker().getName(), equalTo(user.getName()));
        assertThat((responseBookingDto.getStatus()), equalTo(Status.WAITING));
    }

    @Test
    void findByBooker() {
        saveEntity();
        requestBookingDto.setItemId(item.getId());

        for (int i = 0; i < 5; i++) {
           bookingService.createBooking(requestBookingDto, user.getId());
        }

        List<ResponseBookingDto> bookings = bookingService.getBookingsByBooker(user.getId(), State.ALL);
        assertThat(bookings.size(), equalTo(5));
    }

    @Test
    void findByOwner() {
        saveEntity();
        requestBookingDto.setItemId(item.getId());

        for (int i = 0; i < 5; i++) {
            bookingService.createBooking(requestBookingDto, user.getId());
        }

        List<ResponseBookingDto> bookings = bookingService.getBookingsByBooker(user.getId(), State.ALL);
        assertThat(bookings.size(), equalTo(5));
    }

    private void saveEntity() {
        user = userRepository.save(user);
        ownerUser = userRepository.save(ownerUser);
        item = itemRepository.save(item);
    }*/
}