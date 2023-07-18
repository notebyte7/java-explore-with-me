package ru.practicum.dto.participationrequest;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Value
public class EventRequestStatusUpdateRequest {
    @NotNull
    @NotEmpty
    List<Long> requestIds;
    @NotBlank
    UpdatedRequestStatus status;
}
