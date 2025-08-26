package com.server.app.service.srvImpl;

import com.server.app.dto.request.category.CategorySaveRequest;
import com.server.app.dto.request.category.CategoryUpdateRequest;
import com.server.app.dto.response.CategoryDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.CategoryMapper;
import com.server.app.model.Category;
import com.server.app.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategorySrvImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategorySrvImpl categorySrv;

    CategorySaveRequest saveRequest = new CategorySaveRequest();
    CategoryUpdateRequest updateRequest = new CategoryUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setCategoryName("Elektronik");
        saveRequest.setDescription("Elektronik eşyalar");
        saveRequest.setPicture("Resim");

        updateRequest.setCategoryName("Güncel Elektronik");
        updateRequest.setCategoryId(1L);
        updateRequest.setDescription("Güncel Elektronik Eşyalar");
        updateRequest.setPicture("Güncel Resim");

        categoryMapper = new CategoryMapper(categoryRepository);
        categorySrv = new CategorySrvImpl(categoryRepository, categoryMapper);
    }

    @AfterEach
    void tearDown() {
        saveRequest = null;
        updateRequest = null;
        categoryMapper = null;
        categorySrv = null;
    }

    @Nested
    class add {

        @Test
        void isEmptyName() {
            saveRequest.setCategoryName("");

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> categorySrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.EMPTY_NAME);
        }

        @Test
        void isNameOutOfRange() {
            saveRequest.setCategoryName("abcdefghijklmnop");

            BusinessException ex = assertThrows(
                    BusinessException.class,
                    () -> categorySrv.add(saveRequest)
            );

            assertThat(ex.getMessage()).isEqualTo(ResultMessages.C_NAME_OUT_OF_RANGE);
        }

        @Test
        void isSuccess() {
            saveRequest.setCategoryName("Kitaplar");

            when(categoryRepository.save(any(Category.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = categorySrv.add(saveRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.SUCCESS);
        }
    }

    @Nested
    class update {

        @Test
        void isEmptyName() {
            updateRequest.setCategoryName("");

            when(categoryRepository.existsCategoryByCategoryId(updateRequest.getCategoryId())).thenReturn(true);

            BusinessException ex = assertThrows(
                    BusinessException.class,
                    () -> categorySrv.update(updateRequest)
            );

            assertThat(ex.getMessage()).isEqualTo(ResultMessages.EMPTY_NAME);
        }

        @Test
        void isNameOutOfRange() {
            updateRequest.setCategoryName("abcdefghijklmnop");

            when(categoryRepository.existsCategoryByCategoryId(updateRequest.getCategoryId())).thenReturn(true);

            BusinessException ex = assertThrows(
                    BusinessException.class,
                    () -> categorySrv.update(updateRequest)
            );

            assertThat(ex.getMessage()).isEqualTo(ResultMessages.C_NAME_OUT_OF_RANGE);
        }

        @Test
        void isSuccess() {
            updateRequest.setCategoryName("Güncel Kitaplar");

            when(categoryRepository.existsCategoryByCategoryId(updateRequest.getCategoryId())).thenReturn(true);

            when(categoryRepository.save(any(Category.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = categorySrv.update(updateRequest);

            assertThat(response).isNotNull();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_UPDATED);
        }
    }

    @Nested
    class findCategoryById {

        @Test
        void isCategoryNotFound() {
            when(categoryRepository.findCategoryByCategoryId(1L)).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> categorySrv.findCategoryByCategoryId(1L)
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.CATEGORY_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            Category category = new Category();
            category.setCategoryId(1L);
            category.setCategoryName("Elektronik");

            when(categoryRepository.findCategoryByCategoryId(1L)).thenReturn(Optional.of(category));

            DataGenericResponse<CategoryDto> response = categorySrv.findCategoryByCategoryId(1L);

            assertThat(response).isNotNull();
            assertThat(response.getData().getCategoryName()).isEqualTo("Elektronik");
        }
    }

    @Nested
    class delete {

        @Test
        void isCategoryNotFound() {
            when(categoryRepository.existsCategoryByCategoryId(1L)).thenReturn(false);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> categorySrv.deleteCategoryByCategoryId(1L)
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.RECORD_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            when(categoryRepository.existsCategoryByCategoryId(1L)).thenReturn(true);

            GenericResponse response = categorySrv.deleteCategoryByCategoryId(1L);

            assertThat(response).isNotNull();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_DELETED);
        }
    }

    @Nested
    class findAllCategories {

        @Test
        void isSuccess() {
            Category cat1 = new Category();
            cat1.setCategoryId(1L);
            cat1.setCategoryName("Elektronik");

            Category cat2 = new Category();
            cat2.setCategoryId(2L);
            cat2.setCategoryName("Kitaplar");

            when(categoryRepository.findAll()).thenReturn(List.of(cat1, cat2));

            DataGenericResponse<List<CategoryDto>> response = categorySrv.findAllCategories();

            assertThat(response).isNotNull();
            assertThat(response.getData().size()).isEqualTo(2);
        }
    }

    @Nested
    class getCategory {

        @Test
        void isCategoryNotFound() {
            when(categoryRepository.getCategoryByCategoryId(1L)).thenReturn(null);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> categorySrv.getCategory(1L)
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.CATEGORY_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            Category category = new Category();
            category.setCategoryId(1L);
            category.setCategoryName("Elektronik");

            when(categoryRepository.getCategoryByCategoryId(1L)).thenReturn(category);

            Category result = categorySrv.getCategory(1L);

            assertThat(result).isNotNull();
            assertThat(result.getCategoryName()).isEqualTo("Elektronik");
        }
    }
}