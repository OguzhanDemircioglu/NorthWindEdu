package com.server.app.service.srvImpl;

import com.google.common.base.Strings;
import com.server.app.dto.response.CategoryDto;
import com.server.app.dto.request.category.CategorySaveRequest;
import com.server.app.dto.request.category.CategoryUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.CategoryMapper;
import com.server.app.model.Category;
import com.server.app.repository.CategoryRepository;
import com.server.app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategorySrvImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public GenericResponse add(CategorySaveRequest request){
        Category category = mapper.saveEntityFromRequest(request);

        BusinessRules.validate(
                checkCategoryForGeneralValidations(category),
                checkNameValidation(category.getCategoryName())
        );

        repository.save(category);
        return new GenericResponse();
    }

    @Override
    public GenericResponse update(CategoryUpdateRequest request) {
        Category category = mapper.toEntity(request);

        BusinessRules.validate(
                checkCategoryForGeneralValidations(category),
                checkNameValidation(category.getCategoryName())
        );

        repository.save(category);
        return GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).build();
    }

    @Override
    public DataGenericResponse<CategoryDto> findCategoryByCategoryId(Long id) {
        Optional<Category> category = repository.findCategoryByCategoryId(id);
        if (category.isEmpty()) {
            throw new BusinessException(ResultMessages.CATEGORY_NOT_FOUND);
        }

        CategoryDto dto = mapper.toDto(category.get());

        return DataGenericResponse.<CategoryDto>dataBuilder()
                .data(dto)
                .build();
    }

    @Override
    public GenericResponse deleteCategoryByCategoryId(Long id) {
        boolean isExist = repository.existsCategoryByCategoryId(id);
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        repository.deleteCategoryByCategoryId(id);

        return GenericResponse.builder().message(ResultMessages.RECORD_DELETED).build();
    }

    @Override
    public DataGenericResponse<List<CategoryDto>> findAllCategories() {
        List<Category> categories = repository.findAll();
        List<CategoryDto> dtos = categories.stream()
                .map(mapper::toDto)
                .toList();

        return DataGenericResponse.<List<CategoryDto>>dataBuilder()
                .data(dtos)
                .build();
    }

    @Override
    public Category getCategory(Long categoryId) {
        Category category = repository.getCategoryByCategoryId(categoryId);
        if (Objects.isNull(category)) {
            throw new BusinessException(ResultMessages.CATEGORY_NOT_FOUND);
        }
        return category;
    }

    private String checkNameValidation(String name) {
        if (name.length() > 15) {
            return ResultMessages.C_NAME_OUT_OF_RANGE;
        }
        return null;
    }

    private String checkCategoryForGeneralValidations(Category request) {
        if(Strings.isNullOrEmpty(request.getCategoryName())) {
            return ResultMessages.EMPTY_NAME;
        }
        return null;
    }
}
