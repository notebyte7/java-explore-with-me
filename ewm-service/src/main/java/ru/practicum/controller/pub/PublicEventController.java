package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.service.pub.PublicEventService;
import ru.practicum.util.StatsManager;
import ru.practicum.util.mapper.EndpointHitMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import static ru.practicum.util.Format.DATA_FORMAT;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
@Validated
public class PublicEventController {
    private final PublicEventService publicEventService;
    private final StatsManager statsManager;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<EventShortDto> getEvents(@RequestParam(required = false) @Size(min = 1, max = 7000) String text,
                                               @RequestParam(required = false) Set<@Positive Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = DATA_FORMAT) LocalDateTime rangeStart,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = DATA_FORMAT) LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "false", required = false) Boolean onlyAvailable,
                                               @RequestParam(defaultValue = "EVENT_DATE", required = false) String sort,
                                               @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                               @RequestParam(defaultValue = "10", required = false) @Positive Integer size,
                                               HttpServletRequest request) {
        Collection<EventShortDto> events = publicEventService.getEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        statsManager.postHit(EndpointHitMapper.toEndpointHitDto(request));
        return events;
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventFullDto getEventById(@Positive @NotNull @PathVariable Long id,
                                     HttpServletRequest request) {
        EventFullDto event = publicEventService.getEventById(id);
        statsManager.postHit(EndpointHitMapper.toEndpointHitDto(request));
        return event;
    }
}
