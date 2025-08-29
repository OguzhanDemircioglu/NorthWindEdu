package com.server.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.dto.request.customerDemographics.CustomerDemographicsSaveRequest;
import com.server.app.dto.request.customerDemographics.CustomerDemographicsUpdateRequest;
import com.server.app.dto.response.CustomerDemographicsDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.CustomerDemographicsService;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerDemographicsController.class, excludeAutoConfiguration =  SecurityAutoConfiguration.class)
class CustomerDemographicsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerDemographicsService service;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UserService userService;

    CustomerDemographicsSaveRequest saveRequest = new CustomerDemographicsSaveRequest();
    CustomerDemographicsUpdateRequest updateRequest = new CustomerDemographicsUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setCustomerTypeId("1");
        saveRequest.setCustomerDesc("Old");

        updateRequest.setCustomerTypeId("1");
        updateRequest.setCustomerDesc("Updated");
    }

    @AfterEach
    void tearDown() {
        saveRequest = null;
        updateRequest = null;
        service = null;
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

            BDDMockito.given(service.add(Mockito.any(CustomerDemographicsSaveRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(post("/api/customer-demographics/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUCCESS)));

            verify(service, times(1)).add(Mockito.any(CustomerDemographicsSaveRequest.class));
        }

        @Test
        void notValidRequest() throws Exception {
            doThrow(new BusinessException(ResultMessages.CUSTOMER_DEMOGRAPHICS_NOT_FOUND))
                    .when(service).add(Mockito.any(CustomerDemographicsSaveRequest.class));

            mockMvc.perform(post("/api/customer-demographics/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new CustomerDemographicsSaveRequest())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.CUSTOMER_DEMOGRAPHICS_NOT_FOUND)));
        }
    }

    @Nested
    class update {
        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).success(true).build();

            BDDMockito.given(service.update(Mockito.any(CustomerDemographicsUpdateRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(put("/api/customer-demographics/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.RECORD_UPDATED)));

            verify(service, times(1)).update(Mockito.any(CustomerDemographicsUpdateRequest.class));
        }
    }

    @Nested
    class findById {
        @Test
        void isSuccess() throws Exception {
            CustomerDemographicsDto dto = new CustomerDemographicsDto();

            DataGenericResponse<CustomerDemographicsDto> mockResponse = DataGenericResponse.<CustomerDemographicsDto>dataBuilder()
                    .data(dto)
                    .build();

            BDDMockito.given(service.findCustomerDemographicsByCustomerTypeId("1"))
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/customer-demographics/{id}", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)));

            verify(service, Mockito.times(1)).findCustomerDemographicsByCustomerTypeId("1");
        }
    }

    @Nested
    class delete {
        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = GenericResponse.builder().message(ResultMessages.RECORD_DELETED).success(true).build();

            BDDMockito.given(service.deleteCustomerDemographicsByCustomerTypeId("1"))
                    .willReturn(mockResponse);

            mockMvc.perform(delete("/api/customer-demographics/{id}", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(service, times(1)).deleteCustomerDemographicsByCustomerTypeId("1");
        }
    }

    @Nested
    class findAll {
        @Test
        void isSuccess() throws Exception {
            CustomerDemographicsDto dto1 = new CustomerDemographicsDto();
            CustomerDemographicsDto dto2 = new CustomerDemographicsDto();

            List<CustomerDemographicsDto> dtoList = List.of(dto1, dto2);

            DataGenericResponse<List<CustomerDemographicsDto>> mockResponse = DataGenericResponse.<List<CustomerDemographicsDto>>dataBuilder()
                    .data(dtoList)
                    .build();

            BDDMockito.given(service.findAllCustomerDemographics())
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/customer-demographics")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(service, times(1)).findAllCustomerDemographics();
        }
    }

}