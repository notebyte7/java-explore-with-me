package ru.practicum.util;

import lombok.experimental.UtilityClass;
import ru.practicum.model.rating.Rating;

import java.util.Set;

@UtilityClass
public class RatingCalculator {
    public Long calculateRating(Set<Rating> ratings) {
        if (ratings != null && !ratings.isEmpty()) {
            long likes = ratings
                    .stream()
                    .filter(rating -> rating.getIsLike().equals(Boolean.TRUE))
                    .count();
            return likes - (ratings.size() - likes);
        } else {
            return 0L;
        }
    }
}
