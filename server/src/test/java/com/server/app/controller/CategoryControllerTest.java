package com.server.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.dto.request.category.CategorySaveRequest;
import com.server.app.dto.request.category.CategoryUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.CategoryService;
import com.server.app.service.JWTService;
import com.server.app.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UserService userService;

    CategorySaveRequest saveRequest = new CategorySaveRequest();
    CategoryUpdateRequest updateRequest = new CategoryUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setCategoryName("Kategori 1");
        saveRequest.setDescription("Açıklama");
        saveRequest.setPicture("Resim");

        updateRequest.setCategoryName("Güncel Elektronik");
        updateRequest.setCategoryId(1L);
        updateRequest.setDescription("Güncel Elektronik Eşyalar");
        updateRequest.setPicture("Güncel Resim");
    }

    @AfterEach
    void tearDown() {
        saveRequest = null;
        updateRequest = null;
        categoryService = null;
        jwtService = null;
        userService = null;
        mockMvc = null;
        objectMapper = null;
    }

    @Nested
    class add {
        @Test
        void isSuccess() throws Exception {

            GenericResponse mockResponse = new GenericResponse();

            BDDMockito.given(categoryService.add(Mockito.any(CategorySaveRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(post("/api/categories/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUCCESS)));

            verify(categoryService, times(1)).add(Mockito.any(CategorySaveRequest.class));
        }

        @Test
        void isNoRequest() throws Exception {
            doThrow(new BusinessException(ResultMessages.PROCESS_FAILED))
                    .when(categoryService).add(Mockito.any(CategorySaveRequest.class));

            mockMvc.perform(post("/api/categories/add")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.PROCESS_FAILED)));
        }

        @Test
        void notValidRequest() throws Exception {
            doThrow(new BusinessException(ResultMessages.CATEGORY_NOT_FOUND))
                    .when(categoryService).add(Mockito.any(CategorySaveRequest.class));

            mockMvc.perform(post("/api/categories/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new CategorySaveRequest())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.CATEGORY_NOT_FOUND)));
        }
    }

    @Nested
    class update {
        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = new GenericResponse();

            BDDMockito.given(categoryService.update(any()))
                    .willReturn(mockResponse);

            mockMvc.perform(put("/api/categories/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUCCESS)));

            verify(categoryService, times(1)).update(any());
        }

        @Test
        void isNoRequest() throws Exception {
            doThrow(new BusinessException(ResultMessages.PROCESS_FAILED))
                    .when(categoryService).update(any());

            mockMvc.perform(put("/api/categories/update")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.PROCESS_FAILED)));
        }

        @Test
        void notValidRequest() throws Exception {
            doThrow(new BusinessException(ResultMessages.CATEGORY_NOT_FOUND))
                    .when(categoryService).update(Mockito.any(CategoryUpdateRequest.class));

            mockMvc.perform(put("/api/categories/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.containsString(ResultMessages.CATEGORY_NOT_FOUND)));
        }
    }
}