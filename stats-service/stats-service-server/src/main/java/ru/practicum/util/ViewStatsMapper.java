package ru.practicum.util;

import ru.practicum.ViewStatsDto;
import ru.practicum.model.ViewStats;

public class ViewStatsMapper {
    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return new ViewStatsDto(
                viewStats.getApp(),
                viewStats.getUri(),
                viewStats.getHits()
        );
    }
}
