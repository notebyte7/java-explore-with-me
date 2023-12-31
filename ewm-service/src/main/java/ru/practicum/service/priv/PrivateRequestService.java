package ru.practicum.service.priv;

import ru.practicum.dto.participationrequest.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestService {
    List<ParticipationRequestDto> getUserRequestsForEvents(Long userId);

    ParticipationRequestDto postNewRequestForEvent(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestForEvent(Long userId, Long requestId);
}
