package ru.practicum.shareit.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.comment.model.Comment;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c " +
            "from Comment as c " +
            "join c.item as i " +
            "join c.author as a " +
            "where i.id = :itemId ")
    Collection<Comment> findByItemId(@Param("itemId") long itemId);
}