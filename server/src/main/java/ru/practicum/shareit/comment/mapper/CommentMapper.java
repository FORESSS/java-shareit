package ru.practicum.shareit.comment.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.comment.dto.CommentDTO;
import ru.practicum.shareit.comment.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDTO toCommentDto(Comment comment);
}