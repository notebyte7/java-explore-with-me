package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolationException;
import java.util.List;

@RestControllerAdvice(value = "ru.practicum")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({
            WrongStateArgumentException.class,
            BindException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            ServletException.class,
            MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final Exception e) {
        log.warn(e.getMessage(), e);
        return new ApiError(HttpStatus.BAD_REQUEST.toString(),
                "BAD_REQUEST.",
                e.getMessage(),
                List.of(e.getClass().getName()));
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final Exception e) {
        log.warn(e.getMessage(), e);
        return new ApiError(HttpStatus.NOT_FOUND.toString(),
                "NOT_FOUND.",
                e.getMessage(),
                List.of(e.getClass().getName()));
    }

    @ExceptionHandler({ValidationException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleAlreadyExistsException(final Exception e) {
        log.warn(e.getMessage(), e);
        return new ApiError(HttpStatus.CONFLICT.toString(),
                "CONFLICT.",
                e.getMessage(),
                List.of(e.getClass().getName()));
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        log.warn(e.getMessage(), e);
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "INTERNAL_SERVER_ERROR.",
                e.getMessage(),
                List.of(e.getClass().getName(), e.getCause().toString()));
    }
}
