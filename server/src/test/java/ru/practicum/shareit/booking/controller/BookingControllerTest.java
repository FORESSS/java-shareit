package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private RequestBookingDto requestBookingDto;
    private ResponseBookingDto responseBookingDto;

    @BeforeEach
    void setUp() {
        requestBookingDto = RequestBookingDto.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .itemId(1)
                .build();

        responseBookingDto = ResponseBookingDto.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .status(Status.APPROVED)
                .build();
    }

    @Test
    void testGetBookingById() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(responseBookingDto);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBookingsByBooker() throws Exception {
        List<ResponseBookingDto> listBookings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ResponseBookingDto responseBookingDto = ResponseBookingDto.builder()
                    .id(i)
                    .start(LocalDateTime.now())
                    .end(LocalDateTime.now().plusHours(1))
                    .status(Status.APPROVED)
                    .build();
            listBookings.add(responseBookingDto);
        }
        when(bookingService.getBookingsByBooker(1L, State.ALL))
                .thenReturn(listBookings);

        mvc.perform(get("/bookings?state=ALL")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBookingsByOwner() throws Exception {
        List<ResponseBookingDto> listBookings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ResponseBookingDto responseBookingDto = ResponseBookingDto.builder()
                    .id(i)
                    .start(LocalDateTime.now())
                    .end(LocalDateTime.now().plusHours(1))
                    .status(Status.APPROVED)
                    .build();
            listBookings.add(responseBookingDto);
        }
        when(bookingService.getBookingsByOwner(1L, State.ALL))
                .thenReturn(listBookings);

        mvc.perform(get("/bookings/owner?state=ALL")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateBooking() throws Exception {
        when(bookingService.createBooking(1L, requestBookingDto))
                .thenReturn(responseBookingDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(requestBookingDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateBooking() throws Exception {
        when(bookingService.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(responseBookingDto);

        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(requestBookingDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}