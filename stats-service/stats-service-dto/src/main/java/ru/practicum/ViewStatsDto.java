package ru.practicum;

import lombok.Value;

@Value
public class ViewStatsDto {
    String app;
    String uri;
    Long hits;
}
