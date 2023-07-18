package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.service.pub.PublicCompilationService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
@Validated
public class PublicCompilationController {
    private final PublicCompilationService publicCompilationService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        return publicCompilationService.getCompilations(pinned, from, size);
    }

    @GetMapping(value = "/{compId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CompilationDto getCompilationById(@PathVariable @Positive @NotNull Long compId) {
        return publicCompilationService.getCompilationById(compId);
    }
}
