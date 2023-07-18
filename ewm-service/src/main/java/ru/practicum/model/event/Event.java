package ru.practicum.model.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.model.category.Category;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.location.Location;
import ru.practicum.model.participationrequest.ParticipationRequest;
import ru.practicum.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    Long id;

    @NotNull
    @Column(name = "annotation")
    String annotation;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "category", nullable = false)
    Category category;


    @NotNull
    @Column(name = "event_date")
    LocalDateTime eventDate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "initiator", nullable = false)
    User initiator;

    @NotNull
    @Column(name = "title")
    String title;

    @NotNull
    @Column(name = "description")
    String description;

    @NotNull
    @Column(name = "created_on")
    LocalDateTime createdOn;

    @Column(name = "published_on")
    LocalDateTime publishedOn;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "location")
    Location location;

    @NotNull
    @Column(name = "participant_limit", columnDefinition = "bigint default 0")
    Long participantLimit;

    @NotNull
    @Column(name = "paid", columnDefinition = "boolean default false")
    Boolean paid;

    @NotNull
    @Column(name = "request_moderation", columnDefinition = "boolean default true")
    Boolean requestModeration;

    @OneToMany(targetEntity = ParticipationRequest.class,
            mappedBy = "event",
            cascade = CascadeType.ALL)
    Set<ParticipationRequest> requests = new HashSet<>();

    @ManyToMany(targetEntity = Compilation.class,
            mappedBy = "events")
    Set<Compilation> compilations = new HashSet<>();

    @Enumerated(EnumType.STRING)
    EventState state;
}
