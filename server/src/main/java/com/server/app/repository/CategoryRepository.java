package com.server.app.repository;

import com.server.app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Short> {

    Optional<Category> findCategoryByCategoryId(Short id);

    void deleteCategoryByCategoryId(Short id);

    Category getCategoryByCategoryId(Short categoryId);
}
