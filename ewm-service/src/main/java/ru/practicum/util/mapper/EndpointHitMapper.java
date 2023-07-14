package ru.practicum.util.mapper;

import ru.practicum.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.util.Format.DATA_FORMAT;

public class EndpointHitMapper {
    private static final String app = "ewm-service";

    public static EndpointHitDto toEndpointHitDto(HttpServletRequest request) {
        return new EndpointHitDto(app, request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATA_FORMAT)));
    }
}
