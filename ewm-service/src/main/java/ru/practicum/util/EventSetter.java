package ru.practicum.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ViewStatsDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.WrongStateArgumentException;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.util.mapper.EventMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static ru.practicum.util.DateTimeMapper.toLocalDateTime;

@Component
@RequiredArgsConstructor
public class EventSetter {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final StatsManager statsManager;

    public EventFullDto setAndPatch(UpdateEventRequest updateEventRequest, Event event, Duration duration, boolean isAdmin) {
        Event settedEvent = setEventFields(updateEventRequest, event, duration, isAdmin);
        eventRepository.patch(
                settedEvent.getInitiator().getId(),
                settedEvent.getId(),
                settedEvent.getTitle(),
                settedEvent.getAnnotation(),
                settedEvent.getCategory(),
                settedEvent.getDescription(),
                settedEvent.getEventDate(),
                settedEvent.getPublishedOn(),
                settedEvent.getLocation(),
                settedEvent.getParticipantLimit(),
                settedEvent.getPaid(),
                settedEvent.getRequestModeration(),
                settedEvent.getState());
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.findById(settedEvent.getId()).orElseThrow(() -> new NotFoundException("Событие не найдено", new NullPointerException())));
        List<ViewStatsDto> viewStatsDtoList = statsManager.getViewStats(Set.of(settedEvent.getId()));
        eventFullDto.setViews(statsManager.getViewsCount(settedEvent.getId(), viewStatsDtoList));
        return eventFullDto;
    }

    private Event setEventFields(UpdateEventRequest updateEventRequest, Event event, Duration duration, boolean isAdmin) {
        if (updateEventRequest.getTitle() != null && !updateEventRequest.getTitle().isBlank()) {
            event.setTitle(updateEventRequest.getTitle());
        }
        if (updateEventRequest.getAnnotation() != null && !updateEventRequest.getAnnotation().isBlank()) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getDescription() != null && !updateEventRequest.getDescription().isBlank()) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findByCategoryId(updateEventRequest.getCategory()));
        }
        if (updateEventRequest.getEventDate() != null) {
            event.setEventDate(validateEventDate(updateEventRequest.getEventDate(), duration));
        }
        if (updateEventRequest.getLocation() != null) {
            event.setLocation(updateEventRequest.getLocation());
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventRequest.getRequestModeration());
        }
        event.setState(EventValidator.validateAndSetEventState(updateEventRequest, event, isAdmin));
        return event;
    }

    public LocalDateTime validateEventDate(String stringDateTime, Duration duration) {
        LocalDateTime localDateTime = toLocalDateTime(stringDateTime);
        if (localDateTime.isBefore(LocalDateTime.now().plus(duration))) {
            throw new WrongStateArgumentException(
                    "Дата не может быть раньше текущего ", new IllegalArgumentException());
        }
        return localDateTime;
    }
}
