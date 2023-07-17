package ru.practicum.dto.event;

import lombok.Value;
import org.springframework.lang.Nullable;
import ru.practicum.model.location.Location;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static ru.practicum.util.Format.DATA_PATTERN_FORMAT;

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
    @Pattern(regexp = DATA_PATTERN_FORMAT)
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
