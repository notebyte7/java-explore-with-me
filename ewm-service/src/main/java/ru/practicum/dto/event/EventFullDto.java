package ru.practicum.dto.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.location.Location;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    Long id;
    String title;
    String annotation;
    CategoryDto category;
    String description;
    String eventDate;
    String createdOn;
    String publishedOn;
    Location location;
    Boolean paid;
    Boolean requestModeration;
    Long participantLimit;
    UserShortDto initiator;
    String state;
    Long confirmedRequests;
    Long views;

    public EventFullDto(Long id, String title, String annotation, CategoryDto category, String description,
                        String eventDate, String createdOn, String publishedOn, Location location, Boolean paid,
                        Boolean requestModeration, Long participantLimit, UserShortDto initiator, String state,
                        Long confirmedRequests) {
        this.id = id;
        this.title = title;
        this.annotation = annotation;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.createdOn = createdOn;
        this.publishedOn = publishedOn;
        this.location = location;
        this.paid = paid;
        this.requestModeration = requestModeration;
        this.participantLimit = participantLimit;
        this.initiator = initiator;
        this.state = state;
        this.confirmedRequests = confirmedRequests;
    }
}
