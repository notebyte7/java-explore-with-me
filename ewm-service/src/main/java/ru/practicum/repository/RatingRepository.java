package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.rating.Rating;
import ru.practicum.model.rating.RatingId;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {
    @Query("SELECT r " +
            "FROM Rating r " +
            "WHERE r.event.initiator.id IN ?1 OR ?1 IS NULL")
    List<Rating> findAllByInitiatorIds(Set<Long> initiatorIds);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Rating r " +
            "SET r.isLike = ?3 " +
            "WHERE r.user.id = ?1 " +
            "AND r.event.id = ?2")
    void put(Long userId, Long eventId, boolean isLike);

    @Modifying(clearAutomatically = true)
    void deleteByUserIdAndEventId(Long userId, Long eventId);

    Optional<Rating> findById(RatingId ratingId);
}
