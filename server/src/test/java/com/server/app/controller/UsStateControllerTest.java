package com.server.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.dto.request.usState.UsStateSaveRequest;
import com.server.app.dto.request.usState.UsStateUpdateRequest;
import com.server.app.dto.response.UsStateDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.JWTService;
import com.server.app.service.UsStateService;
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

@WebMvcTest(controllers =  UsStateController.class, excludeAutoConfiguration =  SecurityAutoConfiguration.class)
class UsStateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsStateService stateService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UserService userService;

    UsStateSaveRequest saveRequest= new UsStateSaveRequest();
    UsStateUpdateRequest updateRequest= new UsStateUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setStateName("First State");
        saveRequest.setStateAbbr("S1");
        saveRequest.setStateRegion("First Region");

        updateRequest.setStateId(1L);
        updateRequest.setStateName("Second State");
        updateRequest.setStateAbbr("S2");
        updateRequest.setStateRegion("Second Region");
    }

    @AfterEach
    void tearDown() {
        saveRequest = null;
        updateRequest = null;
        stateService = null;
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

            BDDMockito.given(stateService.add(Mockito.any(UsStateSaveRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(post("/api/us-states/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUCCESS))
            );

            verify(stateService, Mockito.times(1)).add(Mockito.any(UsStateSaveRequest.class));
        }

        @Test
        void isInvalidName() throws Exception {
            doThrow(new BusinessException(ResultMessages.STATE_NAME_OUT_OF_RANGE))
                    .when(stateService).add(Mockito.any(UsStateSaveRequest.class));

            mockMvc.perform(post("/api/us-states/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.STATE_NAME_OUT_OF_RANGE)));
        }

        @Test
        void isInvalidRegion() throws Exception {
            doThrow(new BusinessException(ResultMessages.STATE_REGION_OUT_OF_RANGE))
                    .when(stateService).add(Mockito.any(UsStateSaveRequest.class));

            mockMvc.perform(post("/api/us-states/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.STATE_REGION_OUT_OF_RANGE)));
        }

        @Test
        void isInvalidAbbr() throws Exception {
            doThrow(new BusinessException(ResultMessages.STATE_ABBR_OUT_OF_RANGE))
                    .when(stateService).add(Mockito.any(UsStateSaveRequest.class));

            mockMvc.perform(post("/api/us-states/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.STATE_ABBR_OUT_OF_RANGE)));
        }

        @Test
        void isStateNotFound() throws Exception {
            doThrow(new BusinessException(ResultMessages.STATE_NOT_FOUND))
                    .when(stateService).add(Mockito.any(UsStateSaveRequest.class));

            mockMvc.perform(post("/api/us-states/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.STATE_NOT_FOUND)));
        }
    }

    @Nested
    class update {
        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).success(true).build();

            BDDMockito.given(stateService.update(Mockito.any(UsStateUpdateRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(put("/api/us-states/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.RECORD_UPDATED)));

            verify(stateService, Mockito.times(1)).update(Mockito.any(UsStateUpdateRequest.class));
        }

        @Test
        void isEmptyId() throws Exception {
            doThrow(new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED))
                    .when(stateService).update(Mockito.any(UsStateUpdateRequest.class));

            mockMvc.perform(put("/api/us-states/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.ID_IS_NOT_DELIVERED)));
        }
    }

    @Nested
    class findById {
        @Test
        void isSuccess() throws Exception {
            UsStateDto stateDto = new UsStateDto();

            DataGenericResponse<UsStateDto> mockResponse = DataGenericResponse.<UsStateDto>dataBuilder()
                    .data(stateDto)
                    .build();

            BDDMockito.given(stateService.findStateByStateId(1L))
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/us-states/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)));

            verify(stateService, Mockito.times(1)).findStateByStateId(1L);
        }
    }

    @Nested
    class delete {
        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = GenericResponse.builder().message(ResultMessages.RECORD_DELETED).success(true).build();

            BDDMockito.given(stateService.deleteStateByStateId(1L))
                    .willReturn(mockResponse);

            mockMvc.perform(delete("/api/us-states/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(stateService, times(1)).deleteStateByStateId(1L);
        }
    }

    @Nested
    class findAll {
        @Test
        void isSuccess() throws Exception {
            UsStateDto state1 = new UsStateDto();

            UsStateDto state2 = new UsStateDto();

            List<UsStateDto> stateDtoList = List.of(state1, state2);

            DataGenericResponse<List<UsStateDto>> mockResponse = DataGenericResponse.<List<UsStateDto>>dataBuilder()
                    .data(stateDtoList)
                    .build();

            BDDMockito.given(stateService.findAllStates())
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/us-states")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(stateService, times(1)).findAllStates();
        }
    }
}