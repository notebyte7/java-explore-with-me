package ru.practicum.util.mapper;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.model.category.Category;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getCategoryId(),
                category.getName()
        );
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(
                categoryDto.getName()
        );
    }
}
