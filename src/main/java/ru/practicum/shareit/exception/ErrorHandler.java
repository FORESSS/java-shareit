package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        return new ErrorResponse("Ошибка валидации", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ValidationException.class)
    public ErrorResponse handleValidationException(ValidationException ex) {
        log.error(ex.getMessage());
        return new ErrorResponse("Ошибка валидации", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InvalidRequestException.class)
    public ErrorResponse handleInvalidRequestException(InvalidRequestException ex) {
        log.error(ex.getMessage());
        return new ErrorResponse("Некорректный запрос", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidCommentException.class)
    public ErrorResponse handleInvalidCommentException(InvalidCommentException ex) {
        log.error(ex.getMessage());
        return new ErrorResponse("Ошибка добавления комментария", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InvalidBookingException.class)
    public ErrorResponse handleInvalidBookingException(InvalidBookingException ex) {
        log.error(ex.getMessage());
        return new ErrorResponse("Ошибка бронирования", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PermissionDeniedException.class)
    public ErrorResponse handlePermissionDeniedException(PermissionDeniedException ex) {
        log.error(ex.getMessage());
        return new ErrorResponse("Ошибка доступа", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateEmailException.class)
    public ErrorResponse handleDuplicateEmailException(DuplicateEmailException ex) {
        log.error(ex.getMessage());
        return new ErrorResponse("Ошибка валидации", ex.getMessage());
    }
}