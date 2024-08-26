package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Collection<Item> findAllByOwnerId(long ownerId);

    Collection<Item> findAllByRequestId(long requestId);

    @Query("SELECT i FROM Item i WHERE (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) AND i.available = true) " +
            "OR (LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%')) AND i.available = true)")
    Collection<Item> searchByNameOrDescription(@Param("text") String text);
}