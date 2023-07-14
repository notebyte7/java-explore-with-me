package ru.practicum.service.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ViewStatsDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.dto.participationrequest.EventRequestStatusUpdateRequest;
import ru.practicum.dto.participationrequest.EventRequestStatusUpdateResult;
import ru.practicum.dto.participationrequest.ParticipationRequestDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.exception.WrongStateArgumentException;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventState;
import ru.practicum.model.location.Location;
import ru.practicum.model.participationrequest.ParticipationRequestStatus;
import ru.practicum.repository.*;
import ru.practicum.util.EventSetter;
import ru.practicum.util.PageableBuilder;
import ru.practicum.util.StatsManager;
import ru.practicum.util.mapper.EventMapper;
import ru.practicum.util.mapper.ParticipationRequestMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.util.EventUtil.validateEventInitiator;
import static ru.practicum.util.EventUtil.validateEventStatus;
import static ru.practicum.util.Format.DATA_FORMAT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateEventServiceImpl implements PrivateEventService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final StatsManager statsManager;
    private final EventSetter eventSetter;

    @Transactional
    @Override
    public EventFullDto postNewEvent(Long userId, NewEventDto eventDto) {
        Event event = EventMapper.toEvent(eventDto);
        event.setInitiator(userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден", new NullPointerException())));
        checkEventDate(eventDto.getEventDate());
        event.setCategory(categoryRepository.findByCategoryId(eventDto.getCategory()));
        event.setLocation(checkLocation(event.getLocation()));
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(event));
        eventFullDto.setConfirmedRequests(0L);
        eventFullDto.setViews(0L);
        return eventFullDto;
    }

    private void checkEventDate(String eventDate) {
        LocalDateTime time = LocalDateTime.parse(eventDate, DateTimeFormatter.ofPattern(DATA_FORMAT));
        if (time.isBefore(LocalDateTime.now())) {
            throw new WrongStateArgumentException(
                    "Дата не может быть в прошлом ", new IllegalArgumentException());
        }
    }

    private Location checkLocation(Location location) {
        return locationRepository.findByLatAndLon(location.getLat(), location.getLon())
                .orElse(locationRepository.save(location));
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        Set<Long> userEventIds = new HashSet<>();
        List<EventShortDto> events = eventRepository.findEventsByInitiatorId(userId, PageableBuilder.getPageable(from, size))
                .stream()
                .map(event -> {
                    EventShortDto eventShortDto = EventMapper.toEventShortDto(event);
                    userEventIds.add(eventShortDto.getId());
                    return eventShortDto;
                })
                .collect(Collectors.toList());
        List<ViewStatsDto> viewStatsList = statsManager.getViewStats(userEventIds);
        return events.stream()
                .peek(eventShortDto -> eventShortDto.setViews(statsManager.getViewsCount(eventShortDto.getId(), viewStatsList)))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден", new NullPointerException()));
        validateEventInitiator(userId, event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(
                requestRepository.countByEventIdAndStatus(eventId, ParticipationRequestStatus.CONFIRMED));
        eventFullDto.setViews(statsManager.getViewsCount(eventId, statsManager.getViewStats(Set.of(eventId))));
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto patchEventByInitiator(Long userId, Long eventId, UpdateEventRequest updateEventRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет события с таким id" + eventId,  new NullPointerException()));
        validateEventInitiator(userId, event);
        validateEventStatus(event);
        return eventSetter.setAndPatch(updateEventRequest, event, Duration.ofHours(1), false);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        validateEventInitiator(userId, eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено", new NullPointerException())));
        return requestRepository.findAllByEventId(eventId)
                .stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult patchEventRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        patchRequests(userId, eventId, updateRequest);
        return getPatchedRequests(eventId);
    }

    private void patchRequests(Long userId,
                               Long eventId,
                               EventRequestStatusUpdateRequest updateRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено", new NullPointerException()));
        validateEventInitiator(userId, event);
        ParticipationRequestStatus newStatus = ParticipationRequestStatus.valueOf(updateRequest.getStatus().toString());
        if (newStatus.equals(ParticipationRequestStatus.REJECTED)) {
            validateConfirmedRequests(updateRequest.getRequestIds());
            requestRepository.patch(eventId, updateRequest.getRequestIds(), ParticipationRequestStatus.REJECTED);
        } else {
            EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
            Long participantLimit = event.getParticipantLimit();
            if (participantLimit == 0) {
                requestRepository.patch(eventId, updateRequest.getRequestIds(), newStatus);
            } else {
                Long confirmedRequests = eventFullDto.getConfirmedRequests();
                if (confirmedRequests < participantLimit) {
                    List<Long> idsForConfirm = updateRequest.getRequestIds()
                            .stream()
                            .filter(id -> confirmedRequests + 1 <= participantLimit)
                            .collect(Collectors.toList());
                    List<Long> idsForReject = updateRequest.getRequestIds()
                            .stream()
                            .filter(id -> !idsForConfirm.contains(id))
                            .collect(Collectors.toList());
                    requestRepository.patch(eventId, idsForConfirm, ParticipationRequestStatus.CONFIRMED);
                    if (!idsForReject.isEmpty()) {
                        requestRepository.patch(eventId, idsForReject, ParticipationRequestStatus.REJECTED);
                    }
                } else {
                    throw new ValidationException(
                            "Лимит события" + event.getId() + " исчерпан",
                            new IllegalArgumentException());
                }
            }
        }
    }

    private EventRequestStatusUpdateResult getPatchedRequests(Long eventId) {
        List<ParticipationRequestDto> allEventRequests = requestRepository.findAllByEventId(eventId)
                .stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
        List<ParticipationRequestDto> confirmedRequests = allEventRequests
                .stream()
                .filter(request -> request.getStatus().equals(ParticipationRequestStatus.CONFIRMED.toString()))
                .collect(Collectors.toList());
        List<ParticipationRequestDto> rejectedRequests = allEventRequests
                .stream()
                .filter(request -> request.getStatus().equals(ParticipationRequestStatus.REJECTED.toString()))
                .collect(Collectors.toList());
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private void validateConfirmedRequests(List<Long> requestIds) {
        requestRepository.findAllById(requestIds).stream()
                .filter(request -> request.getStatus().equals(ParticipationRequestStatus.CONFIRMED))
                .findAny()
                .ifPresent(request -> {
                    throw new ValidationException(
                            "Нельзя изменить уже утвержденный",
                            new IllegalArgumentException());
                });
    }
}
