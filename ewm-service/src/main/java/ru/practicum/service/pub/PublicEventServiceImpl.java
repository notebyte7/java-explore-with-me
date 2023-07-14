package ru.practicum.service.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ViewStatsDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.WrongStateArgumentException;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventSort;
import ru.practicum.repository.EventRepository;
import ru.practicum.util.StatsManager;
import ru.practicum.util.mapper.EventMapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.util.EventUtil.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicEventServiceImpl implements PublicEventService{
    private final EventRepository eventRepository;
    private final StatsManager statsManager;

    @Override
    public Collection<EventShortDto> getEvents(String text, Set<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                         Integer size) {
        List<Event> allEvents = eventRepository.findAllPublic(text, categories, paid,
                rangeStart, rangeEnd);
        Set<Long> eventIds = allEvents.stream()
                .map(Event::getId)
                .collect(Collectors.toSet());
        List<ViewStatsDto> viewStatsList = statsManager.getViewStats(eventIds);

        List<EventShortDto> eventShortDtoList = allEvents.stream()
                .map(event -> {
                    EventShortDto eventShortDto = EventMapper.toEventShortDto(event);
                    if (onlyAvailable) {
                        return getAvailable(event, eventShortDto);
                    } else {
                        return eventShortDto;
                    }
                })
                .filter(Objects::nonNull)
                .peek(eventShortDto -> eventShortDto.setViews(statsManager.getViewsCount(eventShortDto.getId(), viewStatsList)))
                .collect(Collectors.toList());

        Page<EventShortDto> page = getPageable(
                sortEvents(eventShortDtoList, validateEventSort(sort)),
                from,
                size);
        return page.getContent();
    }

    private EventSort validateEventSort(String sort) {
        try {
            return EventSort.valueOf(sort);
        } catch (IllegalArgumentException e) {
            throw new WrongStateArgumentException("Неправильно задана сортировка", new IllegalArgumentException());
        }
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        Event event = eventRepository.findByIdPublic(eventId).orElseThrow(
                () -> new NotFoundException("Событие " + eventId + " не существует", new NullPointerException()));
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        List<ViewStatsDto> stats = statsManager.getViewStats(Set.of(eventId));
        Long count = statsManager.getViewsCount(eventId,stats);
        eventFullDto.setViews(count);
        return eventFullDto;
    }

}
