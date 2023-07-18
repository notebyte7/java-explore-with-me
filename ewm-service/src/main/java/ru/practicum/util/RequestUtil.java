package ru.practicum.util;

import lombok.RequiredArgsConstructor;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventState;
import ru.practicum.repository.RequestRepository;

@RequiredArgsConstructor
public class RequestUtil {
    private final RequestRepository requestRepository;

    public static void validateUserIsNotInitiator(Long userId, Event event) {
        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Пользователь " + userId +
                    " не инициатор события " + event.getId(),
                    new IllegalArgumentException());
        }
    }

    public static void validateDoubleRequest(Long userId, Event event) {
        event.getRequests()
                .stream()
                .filter(request -> request.getRequester().getId().equals(userId))
                .findAny()
                .ifPresent(request -> {
                    throw new ValidationException("Пользователь " + userId +
                            " уже создал запрос на участие с событии " + event.getId(),
                            new IllegalArgumentException());
                });
    }

    public static void validateEventIsPublished(Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Событие уже опубликовано", new IllegalArgumentException());
        }
    }


}
