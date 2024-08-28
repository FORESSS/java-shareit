package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
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
        user.setName("User");
        user.setEmail("test@test.com");

        ownerUser = new User();
        ownerUser.setName("Owner");
        ownerUser.setEmail("owner@test.com");

        item = new Item();
        item.setOwner(ownerUser);
        item.setName("Item");
        item.setAvailable(true);
        item.setDescription("Description");

        requestBookingDto = RequestBookingDto.builder()
                .start( LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusHours(1))
                .itemId(item.getId())
                .build();
    }

    @Test
    void testGetBookingById() {
        saveEntity();
        requestBookingDto.setItemId(item.getId());
        ResponseBookingDto responseBookingDto = bookingService.createBooking(user.getId(), requestBookingDto);
        responseBookingDto = bookingService.getBookingById(user.getId(), responseBookingDto.getId());
        assertThat(responseBookingDto.getId(), notNullValue());
        assertThat(responseBookingDto.getItem().getName(), equalTo(item.getName()));
        assertThat(responseBookingDto.getBooker().getName(), equalTo(user.getName()));
        assertThat((responseBookingDto.getStatus()), equalTo(Status.WAITING));
    }

    @Test
    void testGetBookingsByBooker() {
        saveEntity();
        requestBookingDto.setItemId(item.getId());
        for (int i = 0; i < 5; i++) {
            bookingService.createBooking(user.getId(), requestBookingDto);
        }
                Collection<ResponseBookingDto> bookings = bookingService.getBookingsByBooker(user.getId(), State.ALL);
              assertThat(bookings.size(), equalTo(5));
    }

    @Test
    void testGetBookingsByOwner() {
        saveEntity();
              requestBookingDto.setItemId(item.getId());
        for (int i = 0; i < 5; i++) {
            bookingService.createBooking(user.getId(), requestBookingDto);
        }
               Collection<ResponseBookingDto> bookings = bookingService.getBookingsByOwner(user.getId(), State.ALL);
               assertThat(bookings.size(), equalTo(5));
    }

    @Test
    void testCreateBooking() {
        saveEntity();
        requestBookingDto.setItemId(item.getId());
        ResponseBookingDto responseBookingDto = bookingService.createBooking(user.getId(), requestBookingDto);
        assertThat(responseBookingDto.getId(), notNullValue());
        assertThat(responseBookingDto.getItem().getName(), equalTo(item.getName()));
        assertThat(responseBookingDto.getBooker().getName(), equalTo(user.getName()));
        assertThat((responseBookingDto.getStatus()), equalTo(Status.WAITING));
    }

    @Test
    void testUpdateBooking() {
        saveEntity();
        requestBookingDto.setItemId(item.getId());
        ResponseBookingDto responseBookingDto = bookingService.createBooking(user.getId(), requestBookingDto);
        responseBookingDto = bookingService.updateBooking( ownerUser.getId(), responseBookingDto.getId(),  true);
        assertThat(responseBookingDto.getId(), notNullValue());
        assertThat(responseBookingDto.getItem().getName(), equalTo(item.getName()));
        assertThat(responseBookingDto.getBooker().getName(), equalTo(user.getName()));
        assertThat((responseBookingDto.getStatus()), equalTo(Status.APPROVED));
    }

    private void saveEntity() {
        user = userRepository.save(user);
        ownerUser = userRepository.save(ownerUser);
        item = itemRepository.save(item);
    }
}