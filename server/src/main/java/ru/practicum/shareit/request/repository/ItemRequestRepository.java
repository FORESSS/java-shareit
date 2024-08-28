package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query("select ir " +
            "from ItemRequest as ir " +
            "join ir.owner as o " +
            "where o.id = :ownerId " +
            "group by ir.id " +
            "order by ir.created desc ")
    Collection<ItemRequest> findByOwnerId(@Param("ownerId") long ownerId);

    @Query("select ir " +
            "from ItemRequest as ir " +
            "join ir.owner as o " +
            "where o.id != :ownerId " +
            "group by ir.id " +
            "order by ir.created desc ")
    Collection<ItemRequest> findByOwnerIdNotEquals(@Param("ownerId") long ownerId);
}