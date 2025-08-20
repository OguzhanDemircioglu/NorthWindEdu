package com.server.app.mapper;

import com.server.app.dto.request.category.CategorySaveRequest;
import com.server.app.dto.request.category.CategoryUpdateRequest;
import com.server.app.dto.response.CategoryDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.model.Category;
import com.server.app.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    private final CategoryRepository repository;

    public CategoryDto toDto(Category request) {
        return CategoryDto.builder()
                .categoryId(request.getCategoryId())
                .categoryName(request.getCategoryName())
                .description(request.getDescription())
                .picture(request.getPicture())
                .build();
    }

    public Category toEntity(CategoryUpdateRequest request) {
        if(request.getCategoryId() == null || request.getCategoryId() == 0) {
            throw new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        boolean isExist = repository.existsCategoryByCategoryId(request.getCategoryId());
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        return updateEntityFromRequest(request);
    }

    private Category updateEntityFromRequest(CategoryUpdateRequest request) {
        return Category.builder()
                .categoryId(request.getCategoryId())
                .categoryName(request.getCategoryName())
                .description(request.getDescription())
                .picture(request.getPicture())
                .build();
    }

    public Category saveEntityFromRequest(CategorySaveRequest request) {
        return Category.builder()
                .categoryName(request.getCategoryName())
                .description(request.getDescription())
                .picture(request.getPicture())
                .build();
    }
}
