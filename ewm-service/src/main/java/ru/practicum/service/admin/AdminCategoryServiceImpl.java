package ru.practicum.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.category.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.util.mapper.CategoryMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCategoryServiceImpl implements AdminCategoryService{
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto postNewCategory(CategoryDto category) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(category)));
    }

    @Override
    public CategoryDto patchCategory(Long catId, CategoryDto category) {
        Category cat = categoryRepository.findByName(category.getName());
        if (cat != null && !cat.getCategoryId().equals(catId)) {
            throw new ValidationException(
                    "Уже занята",
                    new IllegalArgumentException());
        }
        categoryRepository.patch(catId, category.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.findByCategoryId(catId));
    }

    @Override
    public void deleteCategory(Long catId) {
        if (!eventRepository.findByCategoryCategoryId(catId).isEmpty()) {
            throw new ValidationException(
                    "Категория " + catId + " еще используется",
                    new IllegalArgumentException());
        }
        categoryRepository.deleteById(catId);
    }
}
