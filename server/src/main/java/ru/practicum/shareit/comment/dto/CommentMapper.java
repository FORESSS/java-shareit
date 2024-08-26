package ru.practicum.shareit.comment.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.comment.model.Comment;

@Component
public class CommentMapper {
    public static CommentDTO toCommentDto(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}