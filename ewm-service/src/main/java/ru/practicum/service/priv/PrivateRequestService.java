package ru.practicum.service.priv;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.participationrequest.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestService {
    @Transactional(readOnly = true)
    List<ParticipationRequestDto> getUserRequestsForEvents(Long userId);

    @Transactional
    ParticipationRequestDto postNewRequestForEvent(Long userId, Long eventId);

    @Transactional
    ParticipationRequestDto cancelRequestForEvent(Long userId, Long requestId);
}
