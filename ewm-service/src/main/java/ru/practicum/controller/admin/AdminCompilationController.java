package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationDto;
import ru.practicum.service.admin.AdminCompilationService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
@Validated
public class AdminCompilationController {
    private final AdminCompilationService adminCompilationService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationDto postNewCompilation(@Valid @RequestBody NewCompilationDto newCompilation) {
        return adminCompilationService.postNewCompilation(newCompilation);
    }

    @DeleteMapping(value = "/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCompilation(@Positive @NotNull @PathVariable Long compId) {
        adminCompilationService.deleteCompilation(compId);
    }

    @PatchMapping(value = "/{compId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CompilationDto patchCompilation(@Positive @NotNull @PathVariable Long compId,
                                           @Valid @RequestBody UpdateCompilationDto compilation) {
        return adminCompilationService.patchCompilation(compId, compilation);
    }
}
