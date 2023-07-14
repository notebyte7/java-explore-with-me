package ru.practicum.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
