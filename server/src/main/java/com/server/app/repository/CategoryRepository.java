package com.server.app.repository;

import com.server.app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findCategoryByCategoryId(Long id);

    void deleteCategoryByCategoryId(Long id);

    Category getCategoryByCategoryId(Long categoryId);

    boolean existsCategoryByCategoryId(Long categoryId);

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE category_seq RESTART WITH 1", nativeQuery = true)
    void resetCategorySequence();
}
