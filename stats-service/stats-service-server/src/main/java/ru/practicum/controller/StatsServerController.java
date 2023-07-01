package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;

import static ru.practicum.util.Format.DATA_FORMAT;

@RestController
@RequiredArgsConstructor
public class StatsServerController {
    private final StatsService statsService;

    @PostMapping(value = "/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void postEndpointHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        statsService.postEndpointHit(endpointHitDto);
    }

    @GetMapping(value = "/stats")
    public Collection<ViewStatsDto> getViewStats(@RequestParam @DateTimeFormat(pattern = DATA_FORMAT) LocalDateTime start,
                                                 @RequestParam @DateTimeFormat(pattern = DATA_FORMAT) LocalDateTime end,
                                                 @RequestParam(required = false) String[] uris,
                                                 @RequestParam(required = false, defaultValue = "false") boolean unique) {
        return statsService.getViewStats(start, end, uris, unique);
    }
}
