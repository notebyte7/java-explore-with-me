package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.exception.WrongStateArgumentException;
import ru.practicum.util.EndpointHitMapper;
import ru.practicum.repository.StatsRepository;
import ru.practicum.util.ViewStatsMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public void postEndpointHit(EndpointHitDto endpointHitDto) {
        statsRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
    }

    @Override
    public Collection<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        validateDates(start, end);
        if (unique) {
            return statsRepository.getViewStatsUniqueIp(start, end, getUrisList(uris)).stream()
                    .map(ViewStatsMapper::toViewStatsDto)
                    .collect(Collectors.toList());
        } else {
            return statsRepository.getViewStatsNotUniqueIp(start, end, getUrisList(uris)).stream()
                    .map(ViewStatsMapper::toViewStatsDto)
                    .collect(Collectors.toList());
        }
    }

    private void validateDates(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new WrongStateArgumentException("Дата start не может быть после end");
        }
    }

    private List<String> getUrisList(String[] uris) {
        if (uris != null && uris.length != 0) {
            return Arrays.asList(uris);
        } else {
            return null;
        }
    }
}
