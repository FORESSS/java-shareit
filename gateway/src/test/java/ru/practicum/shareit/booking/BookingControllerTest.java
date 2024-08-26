package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.time.LocalDateTime;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingDtoRequest bookingDtoRequest;


    @BeforeEach
    void setUp() {
        bookingDtoRequest = BookingDtoRequest.builder()
                .itemId(1)
                .start(LocalDateTime.parse("2024-08-25T14:00:00"))
                .end(LocalDateTime.parse("2024-08-26T14:00:00"))
                .build();
    }

    /*@Test
    void createBooking_ShouldReturnStatusOk() throws Exception {
        Mockito.when(bookingClient.create(any(BookingDtoRequest.class), eq(1L)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDtoRequest)))
                .andExpect(status().isOk());
    }

   @Test
    void createBooking_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        bookingDtoRequest.setStart(LocalDateTime.parse("2004-08-23T14:00:00"));

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDtoRequest)))
                .andExpect(status().isBadRequest());
    }*/

}
