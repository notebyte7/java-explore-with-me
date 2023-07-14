package ru.practicum.service.admin;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventRequest;

import java.util.List;
import java.util.Set;

public interface AdminEventService {
    @Transactional(readOnly = true)
    List<EventFullDto> getEventFullDtoList(Set<Long> users,
                                           Set<String> states,
                                           Set<Long> categories,
                                           String rangeStart,
                                           String rangeEnd,
                                           Integer from,
                                           Integer size);

    @Transactional
    EventFullDto patchEventById(Long eventId, UpdateEventRequest updateEventRequest);
}
