package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDateDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemService itemService;
    private ItemDto itemDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("Description")
                .available(true)
                .ownerId(1L)
                .build();

        commentDto = CommentDto.builder()
                .id(1L)
                .text("Comment")
                .itemId(1L)
                .build();
    }

    @Test
    void testGetItemById() throws Exception {
        when(itemService.getItemById(anyLong()))
                .thenReturn(itemDto);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemDto.getOwnerId()), Long.class));
    }

    @Test
    void testGetItemsByOwnerId() throws Exception {
        List<ItemWithDateDto> items = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ItemWithDateDto item = ItemWithDateDto.builder()
                    .id(i)
                    .name("name " + i)
                    .description("description")
                    .available(true)
                    .ownerId(1L)
                    .build();
            items.add(item);
        }
        when(itemService.getItemsByOwnerId(anyLong()))
                .thenReturn(items);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)));
    }

    @Test
    void testSearchByText() throws Exception {
        List<ItemDto> items = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ItemDto item = ItemDto.builder()
                    .id(i)
                    .name("name " + i)
                    .description("description")
                    .available(true)
                    .ownerId(1L)
                    .build();
            items.add(item);
        }
        when(itemService.searchByText(any()))
                .thenReturn(items);

        mvc.perform(get("/items/search?text=gre3")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)));
    }

    @Test
    void testCreateItem() throws Exception {
        when(itemService.createItem(anyLong(), any()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemDto.getOwnerId()), Long.class));
    }

    @Test
    void testCreateComment() throws Exception {
        when(itemService.createComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.itemId", is(commentDto.getItemId()), Long.class));
    }

    @Test
    void testUpdateItem() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemDto.getOwnerId()), Long.class));
    }
}