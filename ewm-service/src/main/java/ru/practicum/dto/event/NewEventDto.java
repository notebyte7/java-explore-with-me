package ru.practicum.dto.event;

import lombok.Value;
import ru.practicum.model.location.Location;

import javax.validation.constraints.*;

import static ru.practicum.util.Format.DATA_PATTERN_FORMAT;

@Value
public class NewEventDto {
    @NotBlank
    @Size(min = 3, max = 120)
    String title;
    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;
    @Positive
    Long category;
    @NotBlank
    @Size(min = 20, max = 7000)
    String description;
    @Pattern(regexp = DATA_PATTERN_FORMAT)
    String eventDate;
    @NotNull
    Location location;
    Boolean paid;
    Boolean requestModeration;
    Long participantLimit;
}
