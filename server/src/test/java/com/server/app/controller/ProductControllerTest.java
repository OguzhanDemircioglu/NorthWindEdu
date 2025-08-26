package com.server.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.dto.request.product.ProductSaveRequest;
import com.server.app.dto.request.product.ProductUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.JWTService;
import com.server.app.service.ProductService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UserService userService;

    ProductSaveRequest request = new ProductSaveRequest();
    ProductUpdateRequest requestUpdate = new ProductUpdateRequest();

    @BeforeEach
    void setUp() {
        request.setProductName("Laptop");
        request.setSupplierId(1L);
        request.setCategoryId(1L);
        request.setQuantityPerUnit("10 units");
        request.setUnitPrice(1000.0);
        request.setDiscontinued(1);

        requestUpdate.setProductName("Laptop");
        requestUpdate.setSupplierId(1L);
        requestUpdate.setCategoryId(1L);
        requestUpdate.setQuantityPerUnit("10 units");
        requestUpdate.setUnitPrice(1000.0);
        requestUpdate.setDiscontinued(1);
    }

    @AfterEach
    void tearDown() {
        request = null;
        requestUpdate = null;
        productService = null;
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

            BDDMockito.given(productService.add(Mockito.any(ProductSaveRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(post("/api/products/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUCCESS)));

            verify(productService, times(1)).add(Mockito.any(ProductSaveRequest.class));
        }

        @Test
        void isNoRequest() throws Exception {
            doThrow(new BusinessException(ResultMessages.PROCESS_FAILED))
                    .when(productService).add(Mockito.any(ProductSaveRequest.class));

            mockMvc.perform(post("/api/products/add")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.PROCESS_FAILED)));

        }

        @Test
        void notValidRequest() throws Exception {
            doThrow(new BusinessException(ResultMessages.SUPPLIER_NOT_FOUND))
                    .when(productService).add(Mockito.any(ProductSaveRequest.class));

            mockMvc.perform(post("/api/products/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new ProductSaveRequest())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUPPLIER_NOT_FOUND)));

        }

        @Test
        void isCategoryNotFound() throws Exception {
            doThrow(new BusinessException(ResultMessages.CATEGORY_NOT_FOUND))
                    .when(productService).add(Mockito.any(ProductSaveRequest.class));

            mockMvc.perform(post("/api/products/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.CATEGORY_NOT_FOUND)));
        }
    }

    @Nested
    class update {
        @Test
        void isSuccess() throws Exception {

            GenericResponse mockResponse = GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).success(true).build();

            BDDMockito.given(productService.update(Mockito.any(ProductUpdateRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(put("/api/products/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestUpdate)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.RECORD_UPDATED)));

            verify(productService, times(1)).update(Mockito.any(ProductUpdateRequest.class));
        }
    }
}