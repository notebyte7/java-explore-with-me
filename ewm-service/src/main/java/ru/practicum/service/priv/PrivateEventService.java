package ru.practicum.service.priv;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.dto.participationrequest.EventRequestStatusUpdateRequest;
import ru.practicum.dto.participationrequest.EventRequestStatusUpdateResult;
import ru.practicum.dto.participationrequest.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventService {
    @Transactional
    EventFullDto postNewEvent(Long userId, NewEventDto eventDto);

    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size);

    EventFullDto getUserEventById(Long userId, Long eventId);

    EventFullDto patchEventByInitiator(Long userId, Long eventId, UpdateEventRequest event);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult patchEventRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);

}
