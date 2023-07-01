package ru.practicum.service;

import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.Collection;

public interface StatsService {
    void postEndpointHit(EndpointHitDto endpointHitDto);

    Collection<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique);
}
