package com.server.app.service;

import com.server.app.dto.CategoryDto;
import com.server.app.dto.request.CategorySaveRequest;
import com.server.app.dto.request.CategoryUpdateRequest;
import com.server.app.model.Category;

import java.util.List;

public interface CategoryService {

    String add(CategorySaveRequest request);

    CategoryDto update(CategoryUpdateRequest request);

    CategoryDto findCategoryByCategoryId(Short id);

    void deleteCategoryByCategoryId(Short id);

    List<CategoryDto> findAllCategories();

    Category getCategory(Short categoryId);
}
