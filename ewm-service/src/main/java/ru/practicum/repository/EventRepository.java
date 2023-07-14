package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventState;
import ru.practicum.model.location.Location;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findById(Long id);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE ((UPPER(e.annotation) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(e.description) LIKE UPPER(CONCAT('%', ?1, '%'))) OR ?1 IS NULL) " +
            "AND (e.category.categoryId IN ?2 OR ?2 IS NULL) " +
            "AND (e.paid = ?3 OR ?3 IS NULL) " +
            "AND e.state = ru.practicum.model.event.EventState.PUBLISHED " +
            "AND (e.eventDate BETWEEN ?4 AND ?5 OR e.eventDate > CURRENT_TIMESTAMP)")
    List<Event> findAllPublic(String text, Set<Long> categories, Boolean paid, LocalDateTime rangeStart,
                              LocalDateTime rangeEnd);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.id = ?1 " +
            "AND e.state = ru.practicum.model.event.EventState.PUBLISHED "
    )
    Optional<Event> findByIdPublic(Long id);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE (e.initiator.id IN ?1 OR ?1 IS NULL) " +
            "AND (e.state IN ?2 OR ?2 IS NULL) " +
            "AND (e.category.categoryId IN ?3 OR ?3 IS NULL) " +
            "AND (e.eventDate BETWEEN ?4 AND ?5 OR e.eventDate > CURRENT_TIMESTAMP)")
    Page<Event> findEvents(
            Set<Long> usersIds,
            Set<EventState> states,
            Set<Long> categoryIds,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Pageable pageable);

    Collection<Event> findByCategoryCategoryId(Long id);

    Page<Event> findEventsByInitiatorId(Long userId, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Event e " +
            "SET " +
            "e.title = ?3, " +
            "e.annotation = ?4, " +
            "e.category = ?5, " +
            "e.description = ?6, " +
            "e.eventDate = ?7, " +
            "e.publishedOn = ?8, " +
            "e.location = ?9, " +
            "e.participantLimit = ?10, " +
            "e.paid = ?11, " +
            "e.requestModeration = ?12, " +
            "e.state = ?13 " +
            "WHERE e.id = ?2 " +
            "AND e.initiator.id = ?1")
    void patch(Long userId,
               Long eventId,
               String title,
               String annotation,
               Category category,
               String description,
               LocalDateTime eventDate,
               LocalDateTime publishedOn,
               Location location,
               Long participationLimit,
               Boolean paid,
               Boolean requestModeration,
               EventState state);
}
