package com.server.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.dto.request.supplier.SupplierSaveRequest;
import com.server.app.dto.request.supplier.SupplierUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.JWTService;
import com.server.app.service.SupplierService;
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

@WebMvcTest(controllers = SupplierController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SupplierService supplierService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UserService userService;

    SupplierSaveRequest request = new SupplierSaveRequest();
    SupplierUpdateRequest requestUpdate = new SupplierUpdateRequest();

    @BeforeEach
    void setUp() {
        request.setCompanyName("KAREL");
        request.setContactName("contactname");
        request.setContactTitle("contacttitle");
        request.setAddress("adress");
        request.setCity("tokat");
        request.setRegion("N");
        request.setPostalCode("12345");
        request.setCountry("TURKEY");
        request.setHomepage("homepage//..");
        request.setPhone("424124211");
        request.setFax("31231321367");

        requestUpdate.setCompanyName("GÜNCEL KAREL");
        requestUpdate.setContactName("GÜNCEL contactname");
        requestUpdate.setContactTitle("GÜNCEL contacttitle");
        requestUpdate.setAddress("GÜNCEL adress");
        requestUpdate.setCity("GÜNCEL tokat");
        requestUpdate.setRegion("GÜNCEL N");
        requestUpdate.setPostalCode("00 12345");
        requestUpdate.setCountry("GÜNCEL TURKEY");
        requestUpdate.setHomepage("GÜNCEL homepage//..");
        requestUpdate.setPhone("11 424124211");
        requestUpdate.setFax("11 131231321367");
    }

    @AfterEach
    void tearDown() {
        request = null;
        requestUpdate = null;
        supplierService = null;
        jwtService = null;
        userService = null;
        mockMvc = null;
        objectMapper = null;
    }

    @Nested
    class general {
        @Test
        void isNoRequest() throws Exception {
            doThrow(new BusinessException(ResultMessages.PROCESS_FAILED))
                    .when(supplierService).add(Mockito.any(SupplierSaveRequest.class));

            mockMvc.perform(post("/api/suppliers/add")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.PROCESS_FAILED)));
        }
    }

    @Nested
    class add {
        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = new GenericResponse(); // success=true, message=SUCCESS

            BDDMockito.given(supplierService.add(Mockito.any(SupplierSaveRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(post("/api/suppliers/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUCCESS)));

            verify(supplierService, times(1)).add(Mockito.any(SupplierSaveRequest.class));
        }

        @Test
        void notValidRequest() throws Exception {
            doThrow(new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED))
                    .when(supplierService).add(Mockito.any(SupplierSaveRequest.class));

            mockMvc.perform(post("/api/suppliers/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new SupplierSaveRequest())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.ID_IS_NOT_DELIVERED)));
        }

        @Test
        void isWrongPhoneFormat() throws Exception {
            doThrow(new BusinessException(ResultMessages.WRONG_PHONE_FORMAT))
                    .when(supplierService).add(Mockito.any(SupplierSaveRequest.class));

            mockMvc.perform(post("/api/suppliers/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.WRONG_PHONE_FORMAT)));
        }
    }

    @Nested
    class update {
        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = GenericResponse.builder()
                    .message(ResultMessages.RECORD_UPDATED)
                    .success(true)
                    .build();

            BDDMockito.given(supplierService.update(Mockito.any(SupplierUpdateRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(put("/api/suppliers/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestUpdate)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.RECORD_UPDATED)));

            verify(supplierService, times(1)).update(Mockito.any(SupplierUpdateRequest.class));
        }
    }
}
