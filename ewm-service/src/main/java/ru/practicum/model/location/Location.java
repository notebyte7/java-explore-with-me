package ru.practicum.model.location;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "locations", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "lat")
    BigDecimal lat;

    @Column(name = "lon")
    BigDecimal lon;
}
