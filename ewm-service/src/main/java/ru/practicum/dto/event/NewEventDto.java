package ru.practicum.dto.event;

import lombok.Value;
import ru.practicum.model.location.Location;

import javax.validation.constraints.*;

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
    @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]$")
    String eventDate;
    @NotNull
    Location location;
    Boolean paid;
    Boolean requestModeration;
    Long participantLimit;
}
