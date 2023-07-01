package ru.practicum.util;

import ru.practicum.EndpointHitDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.util.Format.DATA_FORMAT;

public class EndpointHitMapper {

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return new EndpointHit(
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                LocalDateTime.parse(endpointHitDto.getTimestamp(), DateTimeFormatter.ofPattern(DATA_FORMAT))
        );
    }

    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return new EndpointHitDto(
              endpointHit.getApp(),
              endpointHit.getUri(),
              endpointHit.getIp(),
              endpointHit.getTimestamp().format(DateTimeFormatter.ofPattern(DATA_FORMAT))
        );
    }
}
