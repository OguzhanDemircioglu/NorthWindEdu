package com.server.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.dto.request.region.RegionSaveRequest;
import com.server.app.dto.request.region.RegionUpdateRequest;
import com.server.app.dto.response.RegionDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.JWTService;
import com.server.app.service.RegionService;
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

@WebMvcTest(controllers =  RegionController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class RegionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegionService regionService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UserService userService;

    RegionSaveRequest saveRequest = new RegionSaveRequest();
    RegionUpdateRequest updateRequest = new RegionUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setRegionDescription("ilk bölge");

        updateRequest.setRegionId(1L);
        updateRequest.setRegionDescription("ikinci bölge");
    }

    @AfterEach
    void tearDown() {
        saveRequest = null;
        updateRequest = null;
        regionService = null;
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

            BDDMockito.given(regionService.add(Mockito.any(RegionSaveRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(post("/api/regions/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUCCESS)));

            verify(regionService, times(1)).add(Mockito.any(RegionSaveRequest.class));
        }

        @Test
        void isEmptyDescription() throws Exception {
            doThrow(new BusinessException(ResultMessages.EMPTY_DESCRIPTION))
                    .when(regionService).add(Mockito.any(RegionSaveRequest.class));

            mockMvc.perform(post("/api/regions/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.EMPTY_DESCRIPTION)));
        }

        @Test
        void isRegionNotFound() throws Exception {
            doThrow(new BusinessException(ResultMessages.REGION_NOT_FOUND))
                .when(regionService).add(Mockito.any(RegionSaveRequest.class));

            mockMvc.perform(post("/api/regions/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.REGION_NOT_FOUND)));
        }
    }

    @Nested
    class update {

        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).success(true).build();

            BDDMockito.given(regionService.update(Mockito.any(RegionUpdateRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(put("/api/regions/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.RECORD_UPDATED)));

            verify(regionService, times(1)).update(Mockito.any(RegionUpdateRequest.class));
        }

        @Test
        void isEmptyId() throws Exception {
            doThrow(new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED))
                    .when(regionService).update(Mockito.any(RegionUpdateRequest.class));

            mockMvc.perform(put("/api/regions/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.ID_IS_NOT_DELIVERED)));
        }
    }

    @Nested
    class findById {
        @Test
        void isSuccess() throws Exception {
            RegionDto regionDto = new RegionDto();

            DataGenericResponse<RegionDto> mockResponse = DataGenericResponse.<RegionDto>dataBuilder()
                    .data(regionDto)
                    .build();

            BDDMockito.given(regionService.findRegionByRegionId(1L))
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/regions/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(regionService, times(1)).findRegionByRegionId(1L);
        }
    }

    @Nested
    class delete {
        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = GenericResponse.builder().message(ResultMessages.RECORD_DELETED).success(true).build();

            BDDMockito.given(regionService.deleteRegionByRegionId(1L))
                    .willReturn(mockResponse);

            mockMvc.perform(delete("/api/regions/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(regionService, times(1)).deleteRegionByRegionId(1L);
        }
    }

    @Nested
    class findAll {
        @Test
        void isSuccess() throws Exception {
            RegionDto reg1 = new RegionDto();

            RegionDto reg2 = new RegionDto();

            List<RegionDto> regDtoList = List.of(reg1, reg2);

            DataGenericResponse<List<RegionDto>> mockResponse = DataGenericResponse.<List<RegionDto>>dataBuilder()
                    .data(regDtoList)
                    .build();

            BDDMockito.given(regionService.findAllRegions())
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/regions")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(regionService, times(1)).findAllRegions();
        }
    }

}