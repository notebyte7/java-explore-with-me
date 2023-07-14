package ru.practicum;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class EndpointHitDto {
    @NotBlank
    String app;
    @NotBlank
    String uri;
    @NotBlank
    String ip;
    @NotBlank
    String timestamp;
}
