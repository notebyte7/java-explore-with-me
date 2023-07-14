package ru.practicum.service.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.participationrequest.ParticipationRequestDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.exception.WrongStateArgumentException;
import ru.practicum.model.event.Event;
import ru.practicum.model.participationrequest.ParticipationRequest;
import ru.practicum.model.participationrequest.ParticipationRequestStatus;
import ru.practicum.model.user.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.util.mapper.ParticipationRequestMapper;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.util.RequestUtil.*;

@Service
@RequiredArgsConstructor
public class PrivateRequestServiceImpl implements PrivateRequestService{
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getUserRequestsForEvents(Long userId) {
        return requestRepository.findAllByRequesterId(userId)
                .stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto postNewRequestForEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден", new NullPointerException()));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие не найдено", new NullPointerException()));
        validateUserIsNotInitiator(userId, event);
        validateDoubleRequest(userId, event);
        validateEventIsPublished(event);
        validateParticipantLimit(event);
        ParticipationRequest request = new ParticipationRequest(event, user);

        if (event.getParticipantLimit() == 0 || Boolean.FALSE.equals(event.getRequestModeration())) {
            request.setStatus(ParticipationRequestStatus.CONFIRMED);
        } else {
            request.setStatus(ParticipationRequestStatus.PENDING);
        }
        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequestForEvent(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId).orElseThrow(
                () -> new NotFoundException("Запрос" + requestId +
                        " от пользователя " + userId + " не существует", new NullPointerException()));
        if (request.getStatus().equals(ParticipationRequestStatus.CANCELED)) {
            throw new WrongStateArgumentException("Запрос " + requestId +
                    " от пользователя " + userId + " уже отменен", new IllegalArgumentException());
        } else {
            requestRepository.cancel(requestId, userId);
        }
        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository.getReferenceById(requestId));
    }

    private void validateParticipantLimit(Event event) {
        Long participantLimit = event.getParticipantLimit();
        if (participantLimit > 0) {
            Long confirmedRequests = requestRepository.countByEventIdAndStatus(event.getId(), ParticipationRequestStatus.CONFIRMED);
            if (participantLimit.equals(confirmedRequests)) {
                throw new ValidationException("максимальное количество запросов на участие - " + participantLimit,
                        new IllegalArgumentException());
            }
        }
    }

}
