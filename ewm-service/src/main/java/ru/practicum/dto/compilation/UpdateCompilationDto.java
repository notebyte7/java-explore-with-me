package ru.practicum.dto.compilation;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UpdateCompilationDto {
    @Nullable
    @Size(min = 1, max = 50)
    private String title;
    @Nullable
    private Boolean pinned;
    @Nullable
    private Set<Long> events;
}
