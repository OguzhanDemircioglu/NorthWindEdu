package com.server.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.dto.request.shipper.ShipperSaveRequest;
import com.server.app.dto.request.shipper.ShipperUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.JWTService;
import com.server.app.service.ShipperService;
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

@WebMvcTest(controllers = ShipperController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class ShipperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShipperService shipperService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UserService userService;

    ShipperSaveRequest request = new ShipperSaveRequest();
    ShipperUpdateRequest requestUpdate = new ShipperUpdateRequest();

    @BeforeEach
    void setUp() {
        request.setCompanyName("karel");
        request.setPhone("123123123");

        requestUpdate.setShipperId(1L);
        requestUpdate.setCompanyName("karelguncel");
        requestUpdate.setPhone("124124124");
    }
    @AfterEach
    void tearDown() {
        request = null;
        requestUpdate = null;
        mockMvc = null;
        objectMapper = null;
    }
    @Nested
    class add {
        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = new GenericResponse();

            BDDMockito.given(shipperService.add(Mockito.any(ShipperSaveRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(post("/api/shippers/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUCCESS)));

            verify(shipperService, times(1)).add(Mockito.any(ShipperSaveRequest.class));
        }

        @Test
        void notValidRequest_emptyName() throws Exception {

            doThrow(new BusinessException(ResultMessages.EMPTY_NAME))
                    .when(shipperService).add(Mockito.any(ShipperSaveRequest.class));

            mockMvc.perform(post("/api/shippers/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new ShipperSaveRequest())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.EMPTY_NAME)));
        }
        @Test
        void notValidRequest_companyNameOutOfRange() throws Exception {
            doThrow(new BusinessException(ResultMessages.COMPANY_NAME_OUT_OF_RANGE))
                    .when(shipperService).add(Mockito.any(ShipperSaveRequest.class));

            ShipperSaveRequest bad = new ShipperSaveRequest();
            bad.setCompanyName("A".repeat(60));

            mockMvc.perform(post("/api/shippers/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bad)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.COMPANY_NAME_OUT_OF_RANGE)));
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

            BDDMockito.given(shipperService.update(Mockito.any(ShipperUpdateRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(put("/api/shippers/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestUpdate)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.RECORD_UPDATED)));

            verify(shipperService, times(1)).update(Mockito.any(ShipperUpdateRequest.class));
        }
    }
}
