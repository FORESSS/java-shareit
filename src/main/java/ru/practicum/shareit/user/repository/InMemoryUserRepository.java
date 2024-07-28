package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private long currentId = 0;
    Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getUserById(Long userid) {
        return Optional.ofNullable(users.get(userid));
    }

    @Override
    public User createUser(User user) {
        user.setId(generateNewId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) {
        users.put(userId, user);
        return user;
    }

    @Override
    public void deleteUserById(Long userId) {
        users.remove(userId);
    }

    private Long generateNewId() {
        return ++currentId;
    }
}