package com.server.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.dto.request.customerDemo.CustomerDemoSaveRequest;
import com.server.app.dto.request.customerDemo.CustomerDemoUpdateRequest;
import com.server.app.dto.request.territory.TerritorySaveRequest;
import com.server.app.dto.response.CustomerDemoDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.embedded.CustomerDemoId;
import com.server.app.service.CustomerDemoService;
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

@WebMvcTest(controllers = CustomerDemoController.class, excludeAutoConfiguration =  SecurityAutoConfiguration.class)
class CustomerDemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerDemoService customerDemoService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UserService userService;

    CustomerDemoSaveRequest saveRequest = new CustomerDemoSaveRequest();
    CustomerDemoUpdateRequest updateRequest = new CustomerDemoUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setCustomerId("Cus001");
        saveRequest.setCustomerTypeId("Type 1");

        updateRequest.setCustomerId("Cus001");
        updateRequest.setCustomerTypeId("Type 1");
    }

    @AfterEach
    void tearDown() {
        saveRequest =  null;
        updateRequest = null;
        customerDemoService = null;
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

            BDDMockito.given(customerDemoService.add(Mockito.any(CustomerDemoSaveRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(post("/api/customer-demos/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUCCESS)));

            verify(customerDemoService, times(1)).add(Mockito.any(CustomerDemoSaveRequest.class));
        }

        @Test
        void isCustomerNotFound() throws Exception {
            doThrow(new BusinessException(ResultMessages.CUSTOMER_NOT_FOUND))
                    .when(customerDemoService).add(Mockito.any(CustomerDemoSaveRequest.class));

            mockMvc.perform(post("/api/customer-demos/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new CustomerDemoSaveRequest())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.CUSTOMER_NOT_FOUND)));
        }

        @Test
        void isCustomerTypeNotFound() throws Exception {
            doThrow(new BusinessException(ResultMessages.CUSTOMER_DEMOGRAPHICS_NOT_FOUND))
                    .when(customerDemoService).add(Mockito.any(CustomerDemoSaveRequest.class));

            mockMvc.perform(post("/api/customer-demos/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new TerritorySaveRequest())))
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

            BDDMockito.given(customerDemoService.update(Mockito.any(CustomerDemoUpdateRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(put("/api/customer-demos/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.RECORD_UPDATED)));

            verify(customerDemoService, times(1)).update(Mockito.any(CustomerDemoUpdateRequest.class));
        }
    }

    @Nested
    class findById {
        @Test
        void isSuccess() throws Exception {
            CustomerDemoId customerDemoId = new CustomerDemoId("Cus001", "Type 1");
            CustomerDemoDto customerDemoDto = new CustomerDemoDto();

            DataGenericResponse<CustomerDemoDto> mockResponse = DataGenericResponse.<CustomerDemoDto>dataBuilder()
                    .data(customerDemoDto)
                    .build();

            BDDMockito.given(customerDemoService.findCustomerDemoByCustomerDemoId(customerDemoId))
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/customer-demos/")
                            .param("customerId", "Cus001")
                            .param("customerTypeId", "Type 1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)));

            verify(customerDemoService, Mockito.times(1)).findCustomerDemoByCustomerDemoId(customerDemoId);
        }
    }

    @Nested
    class delete {
        @Test
        void isSuccess() throws Exception {
            CustomerDemoId customerDemoId = new CustomerDemoId("Cus001", "Type 1");

            GenericResponse mockResponse = GenericResponse.builder().message(ResultMessages.RECORD_DELETED).success(true).build();

            BDDMockito.given(customerDemoService.deleteCustomerDemoByCustomerDemoId(customerDemoId))
                    .willReturn(mockResponse);

            mockMvc.perform(delete("/api/customer-demos/")
                            .param("customerId", "Cus001")
                            .param("customerTypeId", "Type 1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(customerDemoService, times(1)).deleteCustomerDemoByCustomerDemoId(customerDemoId);
        }
    }

    @Nested
    class findAll {
        @Test
        void isSuccess() throws Exception {
            CustomerDemoDto customerDemoDto1 = new CustomerDemoDto();
            CustomerDemoDto customerDemoDto2 = new CustomerDemoDto();


            List<CustomerDemoDto> customerDemoDtoList = List.of(customerDemoDto1, customerDemoDto2);

            DataGenericResponse<List<CustomerDemoDto>> mockResponse = DataGenericResponse.<List<CustomerDemoDto>>dataBuilder()
                    .data(customerDemoDtoList)
                    .build();

            BDDMockito.given(customerDemoService.findAllCustomerDemos())
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/customer-demos")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(customerDemoService, times(1)).findAllCustomerDemos();
        }
    }
}