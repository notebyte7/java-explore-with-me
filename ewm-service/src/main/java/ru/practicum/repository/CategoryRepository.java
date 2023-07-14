package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryId(Long id);

    Category findByName(String name);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Category c " +
            "SET c.name = ?2 " +
            "WHERE c.categoryId = ?1")
    void patch(Long categoryId, String name);
}
