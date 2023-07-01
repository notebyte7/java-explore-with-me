package ru.practicum.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewStats {
    String app;
    String uri;
    Long hits;
}
