package ru.practicum.dto.participationrequest;

import lombok.Value;

@Value
public class ParticipationRequestDto {
    Long id;
    Long event;
    Long requester;
    String created;
    String status;
}
