package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ItemControllerTest {
    private MockMvc mockMvc;
    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    void create() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @Test
    void testGetAllItems() throws Exception {
        ItemDTO item1 = ItemDTO.builder().id(1).name("Item1").description("Description1").available(true).owner(1).request(1).build();
        ItemDTO item2 = ItemDTO.builder().id(2).name("Item2").description("Description2").available(true).owner(1).request(1).build();
        given(itemService.getAllItems(1L)).willReturn(Arrays.asList(item1, item2));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Item1"))
                .andExpect(jsonPath("$[1].name").value("Item2"));
    }

    @Test
    void testGetItemById() throws Exception {
        ItemDTO item = ItemDTO.builder().id(3).name("Item3").description("Description3").available(true).owner(1).request(1).build();
        given(itemService.getItemById(3L)).willReturn(item);

        mockMvc.perform(get("/items/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Item3"));
    }

    @Test
    void testSearchItems() throws Exception {
        ItemDTO item1 = ItemDTO.builder().id(4).name("Item4").description("Description4").available(true).owner(1).request(1).build();
        given(itemService.searchItems("Item4")).willReturn(Collections.singletonList(item1));

        mockMvc.perform(get("/items/search?text=Item4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Item4"));
    }

    @Test
    void testCreateItem() throws Exception {
        ItemDTO itemDto = ItemDTO.builder().id(5).name("Item5").description("Description5").available(true).owner(1).request(1).build();
        given(itemService.createItem(any(Long.class), any(ItemDTO.class))).willReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Item5"));
        verify(itemService).createItem(any(Long.class), any(ItemDTO.class));
    }

    @Test
    void testUpdateItem() throws Exception {
        ItemUpdateDTO itemUpdateDto = ItemUpdateDTO.builder().name("Item6").description("Updated6").available(true).build();
        ItemDTO updatedItem = ItemDTO.builder().id(1).name("Item7").description("Updated7").available(true).owner(1).request(1).build();
        given(itemService.updateItem(any(Long.class), any(Long.class), any(ItemUpdateDTO.class))).willReturn(updatedItem);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(itemUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Item7"));

        verify(itemService).updateItem(any(Long.class), any(Long.class), any(ItemUpdateDTO.class));
    }

    @Test
    void testDeleteItem() throws Exception {
        mockMvc.perform(delete("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(itemService).deleteItem(any(Long.class), any(Long.class));
    }
}