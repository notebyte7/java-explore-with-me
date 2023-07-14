package ru.practicum.util.mapper;

import ru.practicum.dto.participationrequest.ParticipationRequestDto;
import ru.practicum.model.participationrequest.ParticipationRequest;

import java.time.format.DateTimeFormatter;

import static ru.practicum.util.Format.DATA_FORMAT;

public class ParticipationRequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(
                participationRequest.getId(),
                participationRequest.getEvent().getId(),
                participationRequest.getRequester().getId(),
                participationRequest.getCreated().format(DateTimeFormatter.ofPattern(DATA_FORMAT)),
                participationRequest.getStatus().toString()
        );
    }
}
