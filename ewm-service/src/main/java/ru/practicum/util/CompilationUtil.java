package ru.practicum.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ViewStatsDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.repository.EventRepository;
import ru.practicum.util.mapper.EventMapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationUtil {
    private final EventRepository eventRepository;
    private final StatsManager statsManager;
    public List<EventShortDto> getEventShortList(Set<Long> eventIds) {
        if (eventIds != null) {
            List<ViewStatsDto> viewStatsList = statsManager.getViewStats(eventIds);
            return eventRepository.findAllById(eventIds)
                    .stream()
                    .map(event -> {
                        EventShortDto eventShortDto = EventMapper.toEventShortDto(event);
                        eventShortDto.setViews(statsManager.getViewsCount(eventShortDto.getId(), viewStatsList));
                        return eventShortDto;
                    })
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}
