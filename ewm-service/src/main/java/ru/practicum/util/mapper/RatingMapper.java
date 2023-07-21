package ru.practicum.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.rating.RatingDto;
import ru.practicum.model.rating.Rating;
import ru.practicum.model.rating.RatingType;

@UtilityClass
public class RatingMapper {
    public RatingDto toRatingDto(Rating rating) {
        RatingDto ratingDto = new RatingDto();
        ratingDto.setUserName(rating.getUser().getName());
        ratingDto.setEventTitle(rating.getEvent().getTitle());
        if (Boolean.TRUE.equals(rating.getIsLike())) {
            ratingDto.setRatingType(RatingType.LIKE);
        } else {
            ratingDto.setRatingType(RatingType.DISLIKE);
        }
        return ratingDto;
    }
}
