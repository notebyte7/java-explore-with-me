package ru.practicum.util.mapper;

import org.springframework.format.datetime.joda.DateTimeParser;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.model.event.Event;
import ru.practicum.model.participationrequest.ParticipationRequest;
import ru.practicum.model.participationrequest.ParticipationRequestStatus;

import static ru.practicum.util.Format.DATA_FORMAT;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

public class EventMapper {

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getEventDate().format(DateTimeFormatter.ofPattern(DATA_FORMAT)),
                event.getPaid(),
                getConfirmedRequestsCount(event.getRequests()),
                UserMapper.toUserShortDto(event.getInitiator())
        );
    }

    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getDescription(),
                event.getEventDate().format(DateTimeFormatter.ofPattern(DATA_FORMAT)),
                event.getCreatedOn().format(DateTimeFormatter.ofPattern(DATA_FORMAT)),
                setPublishedOn(event.getPublishedOn()),
                event.getLocation(),
                event.getPaid(),
                event.getRequestModeration(),
                event.getParticipantLimit(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getState().toString(),
                getConfirmedRequestsCount(event.getRequests())
        );
    }

    private static Long getConfirmedRequestsCount(Set<ParticipationRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return 0L;
        } else {
            return requests.stream()
                    .filter(request -> request.getStatus().equals(ParticipationRequestStatus.CONFIRMED))
                    .count();
        }
    }

    private static String setPublishedOn(LocalDateTime time) {
        if (time == null) {
            return null;
        } else {
            return time.format(DateTimeFormatter.ofPattern(DATA_FORMAT));
        }
    }

    public static Event toEvent(NewEventDto eventDto) {
        Event event = new Event();
        event.setTitle(eventDto.getTitle());
        event.setAnnotation(eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription());
        event.setEventDate(LocalDateTime.parse(eventDto.getEventDate(), DateTimeFormatter.ofPattern(DATA_FORMAT)));
        event.setLocation(eventDto.getLocation());
        if (eventDto.getPaid() == null) {
            event.setPaid(false);
        } else {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        } else {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        if (eventDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0L);
        } else {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        return event;
    }
}
