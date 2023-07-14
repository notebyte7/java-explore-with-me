package ru.practicum.dto.event;

import lombok.Value;
import org.springframework.lang.Nullable;
import ru.practicum.model.location.Location;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Value
public class UpdateEventRequest {
    @Nullable
    @Size(min = 3, max = 120)
    String title;
    @Nullable
    @Size(min = 20, max = 2000)
    String annotation;
    @Nullable
    Long category;
    @Nullable
    @Size(min = 20, max = 7000)
    String description;
    @Nullable
    @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]$")
    String eventDate;
    @Nullable
    Location location;
    @Nullable
    Long participantLimit;
    @Nullable
    Boolean paid;
    @Nullable
    Boolean requestModeration;
    @Nullable
    String stateAction;
}
