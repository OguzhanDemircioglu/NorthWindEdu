package com.server.app.service;

import com.server.app.dto.response.CategoryDto;
import com.server.app.dto.request.category.CategorySaveRequest;
import com.server.app.dto.request.category.CategoryUpdateRequest;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.Category;

import java.util.List;

public interface CategoryService {

    GenericResponse add(CategorySaveRequest request);

    GenericResponse update(CategoryUpdateRequest request);

    DataGenericResponse<CategoryDto> findCategoryByCategoryId(Long id);

    GenericResponse deleteCategoryByCategoryId(Long id);

    DataGenericResponse<List<CategoryDto>> findAllCategories();

    Category getCategory(Long categoryId);
}
