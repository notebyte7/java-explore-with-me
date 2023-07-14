package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.service.admin.AdminCategoryService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
@Validated
public class AdminCategoryController {
    private final AdminCategoryService adminCategoryService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryDto postNewCategory(@Valid @RequestBody CategoryDto category) {
        return adminCategoryService.postNewCategory(category);
    }

    @DeleteMapping(value = "/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @NotNull @Positive Long catId) {
        adminCategoryService.deleteCategory(catId);
    }

    @PatchMapping(value = "/{catId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CategoryDto patchCategory(@PathVariable @NotNull @Positive Long catId,
                                     @Valid @RequestBody CategoryDto category) {
        return adminCategoryService.patchCategory(catId, category);
    }
}
