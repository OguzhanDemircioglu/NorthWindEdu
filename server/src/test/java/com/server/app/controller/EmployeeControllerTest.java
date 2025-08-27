package com.server.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.dto.request.employee.EmployeeSaveRequest;
import com.server.app.dto.request.employee.EmployeeUpdateRequest;
import com.server.app.dto.response.EmployeeDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.EmployeeService;
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

@WebMvcTest(controllers = EmployeeController.class, excludeAutoConfiguration =  SecurityAutoConfiguration.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UserService userService;

    EmployeeSaveRequest saveRequest = new EmployeeSaveRequest();
    EmployeeUpdateRequest updateRequest = new EmployeeUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setFirstName("Ahmet");
        saveRequest.setLastName("Yılmaz");

        updateRequest.setFirstName("Mehmet");
        updateRequest.setLastName("Yıldız");
        updateRequest.setEmployeeId(1L);
    }

    @AfterEach
    void tearDown() {
        saveRequest = null;
        updateRequest = null;
        employeeService = null;
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

            BDDMockito.given(employeeService.add(Mockito.any(EmployeeSaveRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(post("/api/employees/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUCCESS)));

            verify(employeeService, times(1)).add(Mockito.any(EmployeeSaveRequest.class));
        }

        @Test
        void isEmployeeNotFound() throws Exception {
            doThrow(new BusinessException(ResultMessages.EMPLOYEE_NOT_FOUND))
                    .when(employeeService).add(Mockito.any(EmployeeSaveRequest.class));

            mockMvc.perform(post("/api/employees/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.EMPLOYEE_NOT_FOUND)));
        }
    }

    @Nested
    class update {
        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).success(true).build();

            BDDMockito.given(employeeService.update(Mockito.any(EmployeeUpdateRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(put("/api/employees/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.RECORD_UPDATED)));

            verify(employeeService, times(1)).update(Mockito.any(EmployeeUpdateRequest.class));
        }
    }

    @Nested
    class findById {
        @Test
        void isSuccess() throws Exception {
            EmployeeDto employeeDto = new EmployeeDto();

            DataGenericResponse<EmployeeDto> mockResponse = DataGenericResponse.<EmployeeDto>dataBuilder()
                    .data(employeeDto)
                    .build();

            BDDMockito.given(employeeService.findEmployeeByEmployeeId(1L))
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/employees/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(employeeService, times(1)).findEmployeeByEmployeeId(1L);
        }
    }

    @Nested
    class delete {
        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = GenericResponse.builder().message(ResultMessages.RECORD_DELETED).success(true).build();

            BDDMockito.given(employeeService.deleteEmployeeByEmployeeId(1L))
                    .willReturn(mockResponse);

            mockMvc.perform(delete("/api/employees/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(employeeService, times(1)).deleteEmployeeByEmployeeId(1L);
        }
    }

    @Nested
    class findAll {
        @Test
        void isSuccess() throws Exception {
            EmployeeDto emp1 = new EmployeeDto();

            EmployeeDto emp2 = new EmployeeDto();

            List<EmployeeDto> empDtoList = List.of(emp1, emp2);

            DataGenericResponse<List<EmployeeDto>> mockResponse = DataGenericResponse.<List<EmployeeDto>>dataBuilder()
                    .data(empDtoList)
                    .build();

            BDDMockito.given(employeeService.findAllEmployees())
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/employees")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(employeeService, times(1)).findAllEmployees();
        }
    }
}