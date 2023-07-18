package ru.practicum.dto.compilation;

import lombok.Data;
import org.springframework.lang.Nullable;
import ru.practicum.dto.event.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CompilationDto {
    @Null
    private Long id;
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
    private Boolean pinned;
    @Nullable
    private List<EventShortDto> events;
}
