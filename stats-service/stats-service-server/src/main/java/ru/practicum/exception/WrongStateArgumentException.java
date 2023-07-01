package ru.practicum.exception;

public class WrongStateArgumentException extends RuntimeException {
    public WrongStateArgumentException(final String message) {
        super(message);
    }
}
