package ru.practicum.util;

import org.apache.commons.lang3.EnumUtils;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.exception.ValidationException;
import ru.practicum.exception.WrongStateArgumentException;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventState;
import ru.practicum.model.event.EventStateAction;

import java.time.LocalDateTime;

public class EventValidator {
    public static EventState validateAndSetEventState(UpdateEventRequest updateEventRequest, Event event, boolean isAdmin) {
        String stateAction = updateEventRequest.getStateAction();
        if (stateAction == null) {
            return event.getState();
        }
        validateStateAction(stateAction);
        EventStateAction eventStateAction = EventStateAction.valueOf(stateAction);
        if (isAdmin) {
            return setEventStateByAdmin(eventStateAction, event);
        } else {
            return setEventStateByUser(eventStateAction, event);
        }
    }

    public static void validateStateAction(String stateAction) {
        if (!EnumUtils.isValidEnum(EventStateAction.class, stateAction)) {
            throw new WrongStateArgumentException(
                    stateAction + " не существует",
                    new IllegalArgumentException());
        }
    }

    private static EventState setEventStateByAdmin(EventStateAction eventStateAction, Event event) {
        switch (eventStateAction) {
            case PUBLISH_EVENT:
                if (event.getState().equals(EventState.PENDING)) {
                    event.setPublishedOn(LocalDateTime.now());
                    return EventState.PUBLISHED;
                } else {
                    throw new ValidationException(
                            "Событие " + event.getId() + " нельзя изменить, оно не pending",
                            new IllegalArgumentException());
                }
            case REJECT_EVENT:
                if (!event.getState().equals(EventState.PUBLISHED)) {
                    return EventState.CANCELED;
                } else {
                    throw new ValidationException(
                            "Событие" + event.getId() + " не может быть изменено, уже опубликовано",
                            new IllegalArgumentException());
                }
            default:
                throw new ValidationException(
                        "Не доступно " + eventStateAction,
                        new IllegalArgumentException());
        }
    }

    private static EventState setEventStateByUser(EventStateAction eventStateAction, Event event) {
        switch (eventStateAction) {
            case SEND_TO_REVIEW:
                if (!event.getState().equals(EventState.PUBLISHED)) {
                    return EventState.PENDING;
                } else {
                    throw new ValidationException(
                            "Событие" + event.getId() + "не может быть изменено, уже опубликовано",
                            new IllegalArgumentException());
                }
            case CANCEL_REVIEW:
                if (!event.getState().equals(EventState.PUBLISHED)) {
                    return EventState.CANCELED;
                } else {
                    throw new ValidationException(
                            "Событие " + event.getId() + " не может быть изменено, уже опубликовано",
                            new IllegalArgumentException());
                }
            default:
                throw new ValidationException(
                        "Не доступно " + eventStateAction,
                        new IllegalArgumentException());
        }
    }

    public static EventState validateState(String state) {
        if (!EnumUtils.isValidEnum(EventState.class, state)) {
            throw new WrongStateArgumentException(
                    "Статус " + state + " не существует",
                    new IllegalArgumentException());
        } else {
            return EventState.valueOf(state);
        }
    }
}
