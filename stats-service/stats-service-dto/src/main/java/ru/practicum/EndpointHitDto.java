package ru.practicum;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Value
public class EndpointHitDto {
    @NotBlank
    String app;
    @NotBlank
    String uri;
    @Pattern(regexp = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$")
    String ip;
    @NotBlank
    String timestamp;
}
