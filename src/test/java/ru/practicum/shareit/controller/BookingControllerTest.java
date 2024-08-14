package ru.practicum.shareit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingDTORequest;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private User user;
    private Item item;
    private BookingDTO bookingDTO;
    private BookingDTORequest bookingDTORequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();

        user = new User(1L, "John Doe", "john.doe@example.com");
        item = new Item(1L, "Item1", "Description1", true, user, null);
        bookingDTO = BookingDTO.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();

        bookingDTORequest = BookingDTORequest.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
    }

    @Test
    void testGetAllBookings() throws Exception {
        List<BookingDTO> bookings = Arrays.asList(bookingDTO);

        when(bookingService.getAllBookings(anyLong(), any(BookingState.class)))
                .thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testGetBookingById() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingDTO);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetAllByOwner() throws Exception {
        List<BookingDTO> bookings = Collections.singletonList(bookingDTO);

        when(bookingService.getAllByOwner(anyLong(), any(BookingState.class)))
                .thenReturn(bookings);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }


    @Test
    void testApproveBooking() throws Exception {
        bookingDTO.setStatus(BookingStatus.APPROVED);

        when(bookingService.approveBooking(anyLong(), anyLong(), any(Boolean.class)))
                .thenReturn(bookingDTO);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }
}