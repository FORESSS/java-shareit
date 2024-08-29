package ru.practicum.shareit.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment commentDtoToComment(CommentDto commentDto);

    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "authorName", source = "author.name")
    CommentDto commentToCommentDto(Comment comment);
}