package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Collection<Item> findByOwnerId(long ownerId);

    @Query("select it " +
            "from Item as it " +
            "join it.owner as u " +
            "where it.available = true and (lower(it.name) like :text or lower(it.description) like :text) ")
    Collection<Item> findBySearch(@Param("text") String text);

    Collection<Item> findByRequestId(long requestId);
}