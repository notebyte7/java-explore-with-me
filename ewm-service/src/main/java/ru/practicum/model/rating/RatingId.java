package ru.practicum.model.rating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingId implements Serializable {
    private static final long serialVersionUID = 5747602877987936004L;
    private Long userId;
    private Long eventId;
}
