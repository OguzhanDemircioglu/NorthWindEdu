package com.server.app.repository;

import com.server.app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findCategoryByCategoryId(Long id);

    void deleteCategoryByCategoryId(Long id);

    Category getCategoryByCategoryId(Long categoryId);

    boolean existsCategoryByCategoryId(Long categoryId);

    @Query("SELECT MAX(c.categoryId) FROM Category c")
    Long findMaxId();
}
