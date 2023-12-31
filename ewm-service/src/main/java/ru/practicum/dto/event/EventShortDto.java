package ru.practicum.dto.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class EventShortDto {
    Long id;
    String title;
    String annotation;
    CategoryDto category;
    String eventDate;
    Boolean paid;
    Long confirmedRequests;
    UserShortDto initiator;
    Long views;
    Long rating;

    public EventShortDto(Long id, String title, String annotation, CategoryDto category, String eventDate, Boolean paid,
                         Long confirmedRequests, UserShortDto initiator) {
        this.id = id;
        this.title = title;
        this.annotation = annotation;
        this.category = category;
        this.eventDate = eventDate;
        this.paid = paid;
        this.confirmedRequests = confirmedRequests;
        this.initiator = initiator;
    }

    public EventShortDto(Long id, String title, String annotation, CategoryDto category, String eventDate, Boolean paid,
                         Long confirmedRequests, UserShortDto initiator, Long rating) {
        this.id = id;
        this.title = title;
        this.annotation = annotation;
        this.category = category;
        this.eventDate = eventDate;
        this.paid = paid;
        this.confirmedRequests = confirmedRequests;
        this.initiator = initiator;
        this.rating = rating;
    }
}
