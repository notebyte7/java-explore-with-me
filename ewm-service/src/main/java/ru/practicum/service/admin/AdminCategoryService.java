package ru.practicum.service.admin;

import ru.practicum.dto.category.CategoryDto;

public interface AdminCategoryService {
    CategoryDto postNewCategory(CategoryDto category);

    CategoryDto patchCategory(Long catId, CategoryDto category);

    void deleteCategory(Long catId);
}
