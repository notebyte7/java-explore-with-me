package ru.practicum.dto.rating;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.model.rating.RatingType;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingDto {
    String userName;
    String eventTitle;
    RatingType ratingType;
}
