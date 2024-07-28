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
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;
import ru.practicum.shareit.user.service.UserService;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void create() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        UserDTO user1 = UserDTO.builder().id(1).name("User1").email("test1@example.com").build();
        UserDTO user2 = UserDTO.builder().id(2).name("User2").email("test2@example.com").build();
        given(userService.getAllUsers()).willReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("User1"))
                .andExpect(jsonPath("$[1].name").value("User2"));
    }

    @Test
    void testGetUserById() throws Exception {
        UserDTO user = UserDTO.builder().id(3).name("User3").email("test3@example.com").build();
        given(userService.getUserById(3L)).willReturn(user);

        mockMvc.perform(get("/users/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("User3"));
    }

    @Test
    void testCreateUser() throws Exception {
        UserDTO userDto = UserDTO.builder().id(4).name("User4").email("test4@example.com").build();
        given(userService.createUser(any(UserDTO.class))).willReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("User4"));
        verify(userService).createUser(any(UserDTO.class));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserUpdateDTO userUpdateDto = UserUpdateDTO.builder().name("User5").email("test5@example.com").build();
        UserDTO updatedUser = UserDTO.builder().id(1).name("User6").email("test6@example.com").build();
        given(userService.updateUser(1L, userUpdateDto)).willReturn(updatedUser);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("User6"));
        verify(userService).updateUser(1L, userUpdateDto);
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userService).deleteUserById(1L);
    }
}