package ru.practicum.util;

import lombok.experimental.UtilityClass;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.event.Event;

@UtilityClass
public class EventInitiatorValidator {
    public void validateInitiator(Long userId, Event event) {
        if (!isInitiator(userId, event)) {
            throw new ValidationException(
                    "Пользователь " + userId + " не является инициатором события" + event.getId(),
                    new IllegalArgumentException());
        }
    }

    public void validateNotInitiator(Long userId, Event event) {
        if (isInitiator(userId, event)) {
            throw new ValidationException(
                    "Пользователь " + userId + " является инициатором события " + event.getId(),
                    new IllegalArgumentException());
        }
    }

    private boolean isInitiator(Long userId, Event event) {
        return event.getInitiator().getId().equals(userId);
    }
}
