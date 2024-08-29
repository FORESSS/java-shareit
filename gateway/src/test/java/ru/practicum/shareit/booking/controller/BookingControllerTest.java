package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.service.BookingClient;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @InjectMocks
    private BookingController bookingController;
    @Mock
    private BookingClient bookingClient;
    private MockMvc mvc;
    private ObjectMapper mapper;
    private RequestBookingDto bookingDto;
    private ResponseBookingDto responseDto;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .build();

        bookingDto = RequestBookingDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .itemId(1)
                .build();

        responseDto = ResponseBookingDto.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();
    }

    @Test
    void getBookingById() throws Exception {
        when(bookingClient.getBookingById(anyLong(), anyLong())).thenReturn(ResponseEntity.ok(responseDto));

        mvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(responseDto)));
    }

    @Test
    void getBookingsByBooker() throws Exception {
        when(bookingClient.getBookingsByBooker(anyLong(), eq(State.ALL))).thenReturn(ResponseEntity.ok(responseDto));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(responseDto)));
    }

    @Test
    void getBookingsByOwner() throws Exception {
        when(bookingClient.getBookingsByOwner(anyLong(), eq(State.ALL))).thenReturn(ResponseEntity.ok(responseDto));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(responseDto)));
    }

    @Test
    void createBooking() throws Exception {
        when(bookingClient.createBooking(anyLong(), any(RequestBookingDto.class))).thenReturn(ResponseEntity.ok(responseDto));

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(responseDto)));
    }

    @Test
    void updateBooking() throws Exception {
        when(bookingClient.updateBooking(anyLong(), anyLong(), eq(true))).thenReturn(ResponseEntity.ok(responseDto));

        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(responseDto)));
    }
}