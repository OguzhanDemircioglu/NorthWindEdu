package com.server.app.service.srvImpl;

import com.server.app.dto.CategoryDto;
import com.server.app.dto.EmployeeDto;
import com.server.app.dto.request.CategorySaveRequest;
import com.server.app.dto.request.CategoryUpdateRequest;
import com.server.app.model.Category;
import com.server.app.model.Employee;
import com.server.app.repository.CategoryRepository;
import com.server.app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategorySrvImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public String add(CategorySaveRequest request){
        try {
            repository.save(
                    Category.builder()
                            .categoryName(request.getCategoryName())
                            .description(request.getDescription())
                            .picture(request.getPicture())
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "İşlem Başarısız";
        }
        return "İşlem Başarılı";
    }

    @Override
    public CategoryDto update(CategoryUpdateRequest request) {
        try {
            Optional<Category> category = repository.findCategoryByCategoryId(request.getCategoryId());
            if (category.isEmpty()) {
                throw new RuntimeException("Update Edilecek Kayıt Bulunamadı");
            }

            category.get().setCategoryName(request.getCategoryName());
            category.get().setDescription(request.getDescription());
            category.get().setPicture(request.getPicture());

            repository.save(category.get());

            return categoryToCategoryDtoMapper(category.get());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("İşlem Başarısız");
        }
    }

    @Override
    public CategoryDto findCategoryByCategoryId(Short id) {
        Optional<Category> category = repository.findCategoryByCategoryId(id);
        if (category.isEmpty()) {
            throw new RuntimeException("Kayıt Bulunamadı");
        }

        return categoryToCategoryDtoMapper(category.get());
    }

    @Override
    public void deleteCategoryByCategoryId(Short id) { repository.deleteCategoryByCategoryId(id); }

    @Override
    public List<CategoryDto> findAllCategories() {
        List<Category> list = repository.findAll();
        List<CategoryDto> result = new ArrayList<>();

        for (Category c : list) {
            CategoryDto dto = categoryToCategoryDtoMapper(c);
            result.add(dto);
        }

        return result;
    }

    private CategoryDto categoryToCategoryDtoMapper(Category c) {
        if (c == null) {
            return null;
        }

        CategoryDto dto = new CategoryDto();
        dto.setCategoryId(c.getCategoryId());
        dto.setCategoryName(c.getCategoryName());
        dto.setDescription(c.getDescription());
        dto.setPicture(c.getPicture());

        return dto;
    }
}
