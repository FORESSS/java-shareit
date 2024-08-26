package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(long ownerId);

    @Query("select it " +
            "from Item as it " +
            "join it.owner as u " +
            "where it.available = true and (lower(it.name) like :text or lower(it.description) like :text) ")
    List<Item> findBySearch(@Param("text") String text);

    List<Item> findByRequestId(long requestId);
}