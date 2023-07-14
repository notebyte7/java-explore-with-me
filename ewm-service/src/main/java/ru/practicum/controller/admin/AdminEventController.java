package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.service.admin.AdminEventService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
@Validated
public class AdminEventController {
    private final AdminEventService adminEventService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<EventFullDto> getEvents(@RequestParam(required = false) Set<Long> users,
                                        @RequestParam(required = false) Set<String> states,
                                        @RequestParam(required = false) Set<Long> categories,
                                        @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]$")
                                        @RequestParam(required = false) String rangeStart,
                                        @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]$")
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        return adminEventService.getEventFullDtoList(users, states, categories,rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(value = "/{eventId}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventFullDto patchEventById(@Positive @NotNull @PathVariable Long eventId,
                                       @Valid @RequestBody UpdateEventRequest event) {
        return adminEventService.patchEventById(eventId, event);
    }
}
