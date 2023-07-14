package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.service.pub.PublicCategoryService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
@Validated
public class PublicCategoryController {
    private final PublicCategoryService publicCategoryService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        return publicCategoryService.getCategories(from, size);
    }

    @GetMapping(value = "/{catId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CategoryDto getCategoryById(@Positive @NotNull @PathVariable Long catId) {
        return publicCategoryService.getCategoryById(catId);
    }
}
