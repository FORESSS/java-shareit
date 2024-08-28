package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

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
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    private UserDto user;

    @BeforeEach
    void beforeEach() {
        user = UserDto.builder()
                .name("User")
                .email("test@test.com")
                .build();
    }

    @Test
    void testGetAllUsers() {
        for (int i = 0; i < 6; i++) {
            userService.createUser(user);
            user.setEmail(i + "test@test.com");
        }
        Collection<UserDto> users = userService.getAllUsers();
        assertThat(users.size(), equalTo(6));
    }

    @Test
    void testGetUserById() {
        UserDto userDto = userService.createUser(user);
        userDto = userService.getUserById(userDto.getId());
        assertThat(userDto.getId(), notNullValue());
        assertThat(userDto.getName(), equalTo(user.getName()));
        assertThat(userDto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void testCreateUser() {
        UserDto userDto = userService.createUser(user);
        assertThat(userDto.getId(), notNullValue());
        assertThat(userDto.getName(), equalTo(user.getName()));
        assertThat(userDto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void testUpdateUser() {
        UserDto userDto = userService.createUser(user);
        user.setEmail("xxxxxx@test.ru");
        userDto = userService.updateUser(userDto.getId(), user);
        assertThat(userDto.getId(), notNullValue());
        assertThat(userDto.getName(), equalTo(user.getName()));
        assertThat(userDto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void deleteUser() {
        long id = 0;
        for (int i = 0; i < 6; i++) {
            UserDto us = userService.createUser(user);
            user.setEmail(i + "test@test.com");
            id = us.getId();
        }
        userService.deleteUser(id);
        Collection<UserDto> users = userService.getAllUsers();
        assertThat(users.size(), equalTo(5));
    }
}