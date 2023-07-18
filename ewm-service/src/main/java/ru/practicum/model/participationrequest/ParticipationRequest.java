package ru.practicum.model.participationrequest;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participation_requests", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "event", nullable = false)
    Event event;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "requester", nullable = false)
    User requester;

    @Column(name = "created")
    LocalDateTime created = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    ParticipationRequestStatus status;

    public ParticipationRequest(Event event, User requester) {
        this.event = event;
        this.requester = requester;
    }
}
