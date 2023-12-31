package ru.practicum.service.admin;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventRequest;

import java.util.List;
import java.util.Set;

public interface AdminEventService {
    List<EventFullDto> getEventFullDtoList(Set<Long> users,
                                           Set<String> states,
                                           Set<Long> categories,
                                           String rangeStart,
                                           String rangeEnd,
                                           Integer from,
                                           Integer size,
                                           Boolean rate);

    EventFullDto patchEventById(Long eventId, UpdateEventRequest updateEventRequest);
}
