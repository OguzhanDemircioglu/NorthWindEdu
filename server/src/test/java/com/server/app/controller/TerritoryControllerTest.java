package com.server.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.dto.request.territory.TerritorySaveRequest;
import com.server.app.dto.request.territory.TerritoryUpdateRequest;
import com.server.app.dto.response.TerritoryDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.JWTService;
import com.server.app.service.TerritoryService;
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

@WebMvcTest(controllers = TerritoryController.class, excludeAutoConfiguration =  SecurityAutoConfiguration.class)
class TerritoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TerritoryService territoryService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UserService userService;

    TerritorySaveRequest saveRequest = new TerritorySaveRequest();
    TerritoryUpdateRequest updateRequest = new TerritoryUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setTerritoryId("Terr1");
        saveRequest.setTerritoryDescription("First Territory");

        updateRequest.setTerritoryId("Terr2");
        updateRequest.setTerritoryDescription("Second Territory");
    }

    @AfterEach
    void tearDown() {
        saveRequest =  null;
        updateRequest = null;
        territoryService = null;
        userService = null;
        jwtService = null;
        mockMvc = null;
        objectMapper = null;
    }

    @Nested
    class add {

        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = new GenericResponse();

            BDDMockito.given(territoryService.add(Mockito.any(TerritorySaveRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(post("/api/territories/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUCCESS)));

            verify(territoryService, times(1)).add(Mockito.any(TerritorySaveRequest.class));
        }

        @Test
        void notValidRequest() throws Exception {
            doThrow(new BusinessException(ResultMessages.TERRITORY_NOT_FOUND))
                    .when(territoryService).add(Mockito.any(TerritorySaveRequest.class));

            mockMvc.perform(post("/api/territories/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new TerritorySaveRequest())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.TERRITORY_NOT_FOUND)));
        }

        @Test
        void isRegionNotFound() throws Exception {
            doThrow(new BusinessException(ResultMessages.REGION_NOT_FOUND))
                    .when(territoryService).add(Mockito.any(TerritorySaveRequest.class));

            mockMvc.perform(post("/api/territories/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.REGION_NOT_FOUND)));
        }

        @Test
        void isInvalidDescription() throws Exception {
            doThrow(new BusinessException(ResultMessages.EMPTY_DESCRIPTION))
                    .when(territoryService).add(Mockito.any(TerritorySaveRequest.class));

            mockMvc.perform(post("/api/territories/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.EMPTY_DESCRIPTION)));
        }

        @Test
        void isInvalidId() throws Exception {
            doThrow(new BusinessException(ResultMessages.TERRITORY_ID_OUT_OF_RANGE))
                    .when(territoryService).add(Mockito.any(TerritorySaveRequest.class));

            mockMvc.perform(post("/api/territories/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.TERRITORY_ID_OUT_OF_RANGE)));
        }

        @Test
        void isEmptyId() throws Exception {
            doThrow(new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED))
                    .when(territoryService).add(Mockito.any(TerritorySaveRequest.class));

            mockMvc.perform(post("/api/territories/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.ID_IS_NOT_DELIVERED)));
        }
    }

    @Nested
    class update {
        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).success(true).build();

            BDDMockito.given(territoryService.update(Mockito.any(TerritoryUpdateRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(put("/api/territories/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.RECORD_UPDATED)));

            verify(territoryService, times(1)).update(Mockito.any(TerritoryUpdateRequest.class));
        }
    }

    @Nested
    class findById {
        @Test
        void isSuccess() throws Exception {
            TerritoryDto territoryDto = new TerritoryDto();

            DataGenericResponse<TerritoryDto> mockResponse = DataGenericResponse.<TerritoryDto>dataBuilder()
                    .data(territoryDto)
                    .build();

            BDDMockito.given(territoryService.findTerritoryByTerritoryId("Terr1"))
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/territories/{id}", "Terr1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)));

            verify(territoryService, Mockito.times(1)).findTerritoryByTerritoryId("Terr1");
        }
    }

    @Nested
    class delete {
        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = GenericResponse.builder().message(ResultMessages.RECORD_DELETED).success(true).build();

            BDDMockito.given(territoryService.deleteTerritoryByTerritoryId("Terr1"))
                    .willReturn(mockResponse);

            mockMvc.perform(delete("/api/territories/{id}", "Terr1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(territoryService, times(1)).deleteTerritoryByTerritoryId("Terr1");
        }
    }

    @Nested
    class findAll {
        @Test
        void isSuccess() throws Exception {
            TerritoryDto terr1 = new TerritoryDto();
            TerritoryDto terr2 = new TerritoryDto();

            List<TerritoryDto> terrDtoList = List.of(terr1, terr2);

            DataGenericResponse<List<TerritoryDto>> mockResponse = DataGenericResponse.<List<TerritoryDto>>dataBuilder()
                    .data(terrDtoList)
                    .build();

            BDDMockito.given(territoryService.findAllTerritories())
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/territories")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(territoryService, times(1)).findAllTerritories();
        }
    }

}