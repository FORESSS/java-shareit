package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.Constants.LOCAL_DATE_TIME;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @InjectMocks
    private ItemRequestController itemRequestController;
    @Mock
    private RequestClient requestClient;
    private ObjectMapper mapper;
    private MockMvc mvc;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mvc = MockMvcBuilders
                .standaloneSetup(itemRequestController)
                .build();

        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Request")
                .created(LOCAL_DATE_TIME)
                .build();
    }

    @Test
    void findRequestById() throws Exception {
        when(requestClient.findRequestById(1L))
                .thenReturn(ResponseEntity.ok(itemRequestDto));

        mvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void findRequestsByUserId() throws Exception {
        List<ItemRequestDto> requests = Collections.singletonList(itemRequestDto);
        when(requestClient.findRequestsByUserId(1L))
                .thenReturn(ResponseEntity.ok(requests));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void findAllRequests() throws Exception {
        List<ItemRequestDto> requests = Collections.singletonList(itemRequestDto);
        when(requestClient.findAllRequests(1L))
                .thenReturn(ResponseEntity.ok(requests));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }
    @Test
    void create() throws Exception {
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }
}