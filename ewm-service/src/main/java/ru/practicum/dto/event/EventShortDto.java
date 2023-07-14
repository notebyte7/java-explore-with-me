package ru.practicum.dto.event;

import lombok.*;
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
}
