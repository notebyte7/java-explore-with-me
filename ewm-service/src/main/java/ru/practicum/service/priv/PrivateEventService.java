package ru.practicum.service.priv;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.dto.participationrequest.EventRequestStatusUpdateRequest;
import ru.practicum.dto.participationrequest.EventRequestStatusUpdateResult;
import ru.practicum.dto.participationrequest.ParticipationRequestDto;
import ru.practicum.dto.rating.RatingDto;

import java.util.List;

public interface PrivateEventService {
    EventFullDto postNewEvent(Long userId, NewEventDto eventDto);

    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size, Boolean asc);

    EventFullDto getUserEventById(Long userId, Long eventId);

    EventFullDto patchEventByInitiator(Long userId, Long eventId, UpdateEventRequest event);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult patchEventRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);

    RatingDto postRating(Long userId, Long eventId, Boolean isLike);

    RatingDto putRating(Long userId, Long eventId, Boolean isLike);

    void deleteRating(Long userId, Long eventId);
}
