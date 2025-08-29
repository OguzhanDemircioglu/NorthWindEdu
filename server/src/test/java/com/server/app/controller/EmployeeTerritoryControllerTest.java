package com.server.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.dto.request.employeeTerritory.EmployeeTerritorySaveRequest;
import com.server.app.dto.request.employeeTerritory.EmployeeTerritoryUpdateRequest;
import com.server.app.dto.response.EmployeeTerritoryDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.embedded.EmployeeTerritoryId;
import com.server.app.service.EmployeeTerritoryService;
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

@WebMvcTest(controllers = EmployeeTerritoryController.class, excludeAutoConfiguration =  SecurityAutoConfiguration.class)
class EmployeeTerritoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeTerritoryService employeeTerritoryService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UserService userService;

    EmployeeTerritorySaveRequest saveRequest =  new EmployeeTerritorySaveRequest();
    EmployeeTerritoryUpdateRequest updateRequest = new EmployeeTerritoryUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setTerritoryId("Terr1");
        saveRequest.setEmployeeId(1L);

        updateRequest.setTerritoryId("Terr2");
        updateRequest.setEmployeeId(2L);
    }

    @AfterEach
    void tearDown() {
        saveRequest = null;
        updateRequest = null;
        employeeTerritoryService = null;
        jwtService = null;
        userService = null;
        objectMapper = null;
        mockMvc = null;
    }

    @Nested
    class add {

        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = new GenericResponse();

            BDDMockito.given(employeeTerritoryService.add(Mockito.any(EmployeeTerritorySaveRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(post("/api/employee-territories/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUCCESS)));

            verify(employeeTerritoryService, times(1)).add(Mockito.any(EmployeeTerritorySaveRequest.class));
        }

        @Test
        void isEmployeeNotFound() throws Exception {
            doThrow(new BusinessException(ResultMessages.EMPLOYEE_NOT_FOUND))
                    .when(employeeTerritoryService).add(Mockito.any(EmployeeTerritorySaveRequest.class));

            mockMvc.perform(post("/api/employee-territories/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new EmployeeTerritorySaveRequest())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.EMPLOYEE_NOT_FOUND)));
        }

        @Test
        void isTerritoryNotFound() throws Exception {
            doThrow(new BusinessException(ResultMessages.TERRITORY_NOT_FOUND))
                    .when(employeeTerritoryService).add(Mockito.any(EmployeeTerritorySaveRequest.class));

            mockMvc.perform(post("/api/employee-territories/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new EmployeeTerritorySaveRequest())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.TERRITORY_NOT_FOUND)));
        }
    }

    @Nested
    class update {
        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).success(true).build();

            BDDMockito.given(employeeTerritoryService.update(Mockito.any(EmployeeTerritoryUpdateRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(put("/api/employee-territories/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.RECORD_UPDATED)));

            verify(employeeTerritoryService, times(1)).update(Mockito.any(EmployeeTerritoryUpdateRequest.class));
        }
    }

    @Nested
    class findById {
        @Test
        void isSuccess() throws Exception {
            EmployeeTerritoryId employeeTerritoryId = new EmployeeTerritoryId(1L, "Terr 1");
            EmployeeTerritoryDto employeeTerritoryDto = new EmployeeTerritoryDto();

            DataGenericResponse<EmployeeTerritoryDto> mockResponse = DataGenericResponse.<EmployeeTerritoryDto>dataBuilder()
                    .data(employeeTerritoryDto)
                    .build();

            BDDMockito.given(employeeTerritoryService.findEmployeeTerritoryByEmployeeTerritoryId(employeeTerritoryId))
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/employee-territories/")
                            .param("employeeId", String.valueOf(1L))
                            .param("territoryId", "Terr 1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)));

            verify(employeeTerritoryService, Mockito.times(1)).findEmployeeTerritoryByEmployeeTerritoryId(employeeTerritoryId);
        }
    }

    @Nested
    class delete {
        @Test
        void isSuccess() throws Exception {
            EmployeeTerritoryId employeeTerritoryId = new EmployeeTerritoryId(1L, "Terr 1");

            GenericResponse mockResponse = GenericResponse.builder().message(ResultMessages.RECORD_DELETED).success(true).build();

            BDDMockito.given(employeeTerritoryService.deleteEmployeeTerritoryByEmployeeTerritoryId(employeeTerritoryId))
                    .willReturn(mockResponse);

            mockMvc.perform(delete("/api/employee-territories/")
                            .param("employeeId", String.valueOf(1L))
                            .param("territoryId", "Terr 1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(employeeTerritoryService, times(1)).deleteEmployeeTerritoryByEmployeeTerritoryId(employeeTerritoryId);
        }
    }

    @Nested
    class findAll {
        @Test
        void isSuccess() throws Exception {
            EmployeeTerritoryDto dto1 = new EmployeeTerritoryDto();
            EmployeeTerritoryDto dto2 = new EmployeeTerritoryDto();


            List<EmployeeTerritoryDto> dtoList = List.of(dto1, dto2);

            DataGenericResponse<List<EmployeeTerritoryDto>> mockResponse = DataGenericResponse.<List<EmployeeTerritoryDto>>dataBuilder()
                    .data(dtoList)
                    .build();

            BDDMockito.given(employeeTerritoryService.findAllEmployeeTerritories())
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/employee-territories")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(employeeTerritoryService, times(1)).findAllEmployeeTerritories();
        }
    }

}