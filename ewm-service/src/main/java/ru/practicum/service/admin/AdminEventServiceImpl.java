package ru.practicum.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ViewStatsDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventState;
import ru.practicum.repository.EventRepository;
import ru.practicum.util.*;
import ru.practicum.util.mapper.EventMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.util.DateTimeMapper.toLocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final StatsManager statsManager;
    private final EventSetter eventSetter;

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> getEventFullDtoList(Set<Long> users,
                                                  Set<String> states,
                                                  Set<Long> categories,
                                                  String rangeStart,
                                                  String rangeEnd,
                                                  Integer from,
                                                  Integer size,
                                                  Boolean asc) {
        List<Event> events = getEventList(users, states, categories, rangeStart, rangeEnd, from, size);
        Set<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toSet());
        List<ViewStatsDto> viewStatsList = statsManager.getViewStats(eventIds);
        List<EventFullDto> eventFullDtoList = events.stream()
                .map(event -> {
                    EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
                    eventFullDto.setViews(statsManager.getViewsCount(eventFullDto.getId(), viewStatsList));
                    return eventFullDto;
                })
                .collect(Collectors.toList());
        return getSortedEventsFull(eventFullDtoList, asc);
    }

    private List<Event> getEventList(Set<Long> users,
                                     Set<String> states,
                                     Set<Long> categories,
                                     String rangeStart,
                                     String rangeEnd,
                                     Integer from,
                                     Integer size) {
        LocalDateTime start = toLocalDateTime(rangeStart);
        LocalDateTime end = toLocalDateTime(rangeEnd);
        Pageable pageable = PageableBuilder.getPageable(from, size);
        return eventRepository.findEvents(users, getEventStateSet(states), categories, start, end, pageable).getContent();
    }

    private Set<EventState> getEventStateSet(Set<String> stringStates) {
        if (stringStates != null && !stringStates.isEmpty()) {
            return stringStates.stream()
                    .map(EventValidator::validateState)
                    .collect(Collectors.toSet());
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public EventFullDto patchEventById(Long eventId, UpdateEventRequest updateEventRequest) {
        return eventSetter.setAndPatch(updateEventRequest, eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие не найдено", new NullPointerException())), Duration.ofHours(1), true);
    }

    public List<EventFullDto> getSortedEventsFull(List<EventFullDto> eventFullDtoList, Boolean asc) {
        if (asc != null) {
            if (asc.equals(Boolean.TRUE)) {
                return eventFullDtoList
                        .stream()
                        .sorted(Comparator.comparing(EventFullDto::getRating, Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            } else {
                return eventFullDtoList
                        .stream()
                        .sorted(Comparator.comparing(EventFullDto::getRating))
                        .collect(Collectors.toList());
            }
        } else {
            return eventFullDtoList;
        }
    }
}
