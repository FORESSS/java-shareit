package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = {"spring.datasource.driver-class-name=org.h2.Driver",
                "spring.datasource.url=jdbc:h2:mem:shareit",
                "spring.datasource.username=dbuser",
                "spring.datasource.password=12345"},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    private UserDto user;

    @BeforeEach
    void beforeEach() {
        user = UserDto.builder()
                .name("Max")
                .email("max@ya.ru")
                .build();
    }

    @Test
    void create() {
        UserDto userDto = userService.createUser(user);
        assertThat(userDto.getId(), notNullValue());
        assertThat(userDto.getName(), equalTo(user.getName()));
        assertThat(userDto.getEmail(), equalTo(user.getEmail()));
    }

   /* @Test
    void update() {
        UserDto userDto = userService.createUser(user);
        user.setEmail("greg@ya.ru");

        userDto = userService.updateUser(user, userDto.getId());
        assertThat(userDto.getId(), notNullValue());
        assertThat(userDto.getName(), equalTo(user.getName()));
        assertThat(userDto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void updateFailUser() {
        UserDto userDto = userService.createUser(user);
        user.setEmail("greg@ya.ru");

        assertThatThrownBy(() -> userService.updateUser(user, 3L));
    }

    @Test
    void findById() {
        UserDto userDto = userService.createUser(user);

        userDto = userService.getUserById(userDto.getId());
        assertThat(userDto.getId(), notNullValue());
        assertThat(userDto.getName(), equalTo(user.getName()));
        assertThat(userDto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void findAll() {
        for (int i = 0; i < 6; i++) {
            userService.createUser(user);
            user.setEmail(i + "max@ya.ru");
        }

        List<UserDto> users = userService.getAllUsers();
        assertThat(users.size(), equalTo(6));
    }

    @Test
    void delete() {
        long id = 0;
        for (int i = 0; i < 6; i++) {
            UserDto us = userService.createUser(user);
            user.setEmail(i + "max@ya.ru");
            id = us.getId();
        }

        userService.deleteUser(id);
        List<UserDto> users = userService.getAllUsers();
        assertThat(users.size(), equalTo(5));
    }*/
}