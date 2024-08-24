package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.comment.dto.CommentDTO;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(ItemController.class)
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper objectMapper;
    private ItemDTO itemDTO;
    private CommentDTO commentDTO;
    private Comment comment;

    @BeforeEach
    void create() {
        itemDTO = ItemDTO.builder()
                .id(1L)
                .name("Item")
                .description("Description")
                .available(true)
                .build();

        commentDTO = CommentDTO.builder()
                .id(1L)
                .text("ItemDTO")
                .authorName("User")
                .build();

        comment = Comment.builder()
                .id(1L)
                .text("Comment")
                .build();
    }

    @Test
    void testGetAllItems() throws Exception {
        when(itemService.getAllItems(anyLong())).thenReturn(List.of(itemDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(itemDTO))));
    }

    @Test
    void testGetItemById() throws Exception {
        when(itemService.getItemById(1L)).thenReturn(itemDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(itemDTO)));
    }

    @Test
    void testSearchItems() throws Exception {
        when(itemService.searchItems("Item")).thenReturn(List.of(itemDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/items/search")
                        .param("text", "Item")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(itemDTO))));
    }

    @Test
    void testCreateItem() throws Exception {
        when(itemService.createItem(anyLong(), any(ItemDTO.class))).thenReturn(itemDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(itemDTO)));
    }

    @Test
    void testUpdateItem() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any(ItemDTO.class))).thenReturn(itemDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(itemDTO)));
    }

    @Test
    void testDeleteItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testAddComment() throws Exception {
        when(itemService.addComment(anyLong(), anyLong(), any(Comment.class))).thenReturn(commentDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(commentDTO)));
    }
}