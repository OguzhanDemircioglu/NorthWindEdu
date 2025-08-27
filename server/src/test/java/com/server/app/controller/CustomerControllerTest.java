package com.server.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.dto.request.customer.CustomerSaveRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.CustomerService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UserService userService;

    CustomerSaveRequest request = new CustomerSaveRequest();

    @BeforeEach
    void setUp() {
        request.setCustomerId("CUSTOMERID");
        request.setCompanyName("KAREL");
        request.setContactName("ahmet");
        request.setContactTitle("sales");
        request.setAddress("adress");
        request.setCity("ankara");
        request.setRegion("a");
        request.setPostalCode("60");
        request.setCountry("turkiye");
        request.setPhone("0555 444 22 11");
        request.setFax("0212 123 45 67");
    }

    @AfterEach
    void tearDown() {
        request = null;
        customerService = null;
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

            BDDMockito.given(customerService.add(Mockito.any(CustomerSaveRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(post("/api/customers/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUCCESS)));

            verify(customerService, times(1)).add(Mockito.any(CustomerSaveRequest.class));
        }

        @Test
        void isNoRequest() throws Exception {
            doThrow(new BusinessException(ResultMessages.PROCESS_FAILED))
                    .when(customerService).add(Mockito.any(CustomerSaveRequest.class));

            mockMvc.perform(post("/api/customers/add")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.PROCESS_FAILED)));
        }

        @Test
        void notValidRequest() throws Exception {
            doThrow(new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED))
                    .when(customerService).add(Mockito.any(CustomerSaveRequest.class));

            mockMvc.perform(post("/api/customers/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new CustomerSaveRequest())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.ID_IS_NOT_DELIVERED)));
        }

        @Test
        void isWrongPhoneFormat() throws Exception {
            doThrow(new BusinessException(ResultMessages.WRONG_PHONE_FORMAT))
                    .when(customerService).add(Mockito.any(CustomerSaveRequest.class));

            CustomerSaveRequest badReq = new CustomerSaveRequest();
            badReq.setCustomerId("CUSTOMERID");
            badReq.setCompanyName("KAREL");
            badReq.setPhone("abcde"); //hatalı örnek

            mockMvc.perform(post("/api/customers/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(badReq)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.WRONG_PHONE_FORMAT)));
        }
    }
}
