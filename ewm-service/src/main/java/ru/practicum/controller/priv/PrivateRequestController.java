package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.participationrequest.ParticipationRequestDto;
import ru.practicum.service.priv.PrivateRequestService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
@Validated
public class PrivateRequestController {
    private final PrivateRequestService privateRequestService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<ParticipationRequestDto> getUserRequestsForEvents(@Positive @NotNull @PathVariable Long userId) {
        return privateRequestService.getUserRequestsForEvents(userId);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ParticipationRequestDto postNewRequestForEvent(@Positive @NotNull @PathVariable Long userId,
                                                          @Positive @NotNull @RequestParam Long eventId) {
        return privateRequestService.postNewRequestForEvent(userId, eventId);
    }

    @PatchMapping(value = "/{requestId}/cancel")
    @ResponseStatus(value = HttpStatus.OK)
    public ParticipationRequestDto cancelRequestForEvent(@Positive @NotNull @PathVariable Long userId,
                                                         @Positive @NotNull @PathVariable Long requestId) {
        return privateRequestService.cancelRequestForEvent(userId, requestId);
    }
}
