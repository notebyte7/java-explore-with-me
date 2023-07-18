package ru.practicum.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventSort;
import ru.practicum.model.event.EventState;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EventUtil {

    public static EventShortDto getAvailable(Event event, EventShortDto eventShortDto) {
        if (event.getParticipantLimit() == 0 || event.getParticipantLimit() < eventShortDto.getConfirmedRequests()) {
            return eventShortDto;
        } else {
            return null;
        }
    }

    public static List<EventShortDto> sortEvents(List<EventShortDto> events, EventSort sort) {
        switch (sort) {
            case EVENT_DATE:
                return events.stream()
                        .sorted(Comparator.comparing(EventShortDto::getEventDate))
                        .collect(Collectors.toList());
            case VIEWS:
                return events.stream()
                        .sorted(Comparator.comparing(EventShortDto::getViews))
                        .collect(Collectors.toList());
            default:
                throw new NotFoundException(sort + " is not supported",
                        new NullPointerException());
        }
    }

    public static Page<EventShortDto> getPageable(List<EventShortDto> eventShortDtoList, Integer from, Integer size) {
        Pageable pageable = PageableBuilder.getPageable(from, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), eventShortDtoList.size());
        return new PageImpl<>(eventShortDtoList.subList(start, end), pageable, eventShortDtoList.size());
    }

    public static void validateEventStatus(Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException(
                    event.getId() + " не может быть изменено",
                    new IllegalArgumentException());
        }
    }

    public static void validateEventInitiator(Long userId, Event event) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException(
                    "Пользователь" + userId + " не является инициатором события " + event.getId(),
                    new IllegalArgumentException());
        }
    }
}
