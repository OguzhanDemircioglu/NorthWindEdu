package com.server.app.service.srvImpl;

import com.server.app.dto.request.territory.TerritorySaveRequest;
import com.server.app.dto.request.territory.TerritoryUpdateRequest;
import com.server.app.dto.response.TerritoryDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.TerritoryMapper;
import com.server.app.model.Region;
import com.server.app.model.Territory;
import com.server.app.repository.TerritoryRepository;
import com.server.app.service.RegionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TerritorySrvImplTest {

    @Mock
    private TerritoryRepository territoryRepository;

    @Mock
    private RegionService regionService;

    @InjectMocks
    private TerritoryMapper territoryMapper;

    @InjectMocks
    private TerritorySrvImpl territorySrv;

    TerritorySaveRequest saveRequest = new TerritorySaveRequest();
    TerritoryUpdateRequest updateRequest = new TerritoryUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setTerritoryId("Terr1");
        saveRequest.setTerritoryDescription("First Territory");
        saveRequest.setRegionId(1L);

        updateRequest.setTerritoryId("Terr2");
        updateRequest.setTerritoryDescription("Second Territory");
        updateRequest.setRegionId(2L);

        territoryMapper = new TerritoryMapper(territoryRepository, regionService);
        territorySrv = new TerritorySrvImpl(territoryRepository, territoryMapper);
    }

    @AfterEach
    void tearDown() {
        saveRequest = null;
        updateRequest = null;
        territoryMapper = null;
        territorySrv = null;
    }

    @Nested
    class add {

        @Test
        void isEmptyId() {
            saveRequest.setTerritoryId("");

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> territorySrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        @Test
        void isEmptyDescription() {
            saveRequest.setTerritoryDescription("");

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> territorySrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.EMPTY_T_DESCRIPTION);
        }

        @Test
        void isInvalidId() {

            saveRequest.setTerritoryId("t".repeat(21));

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> territorySrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.TERRITORY_ID_OUT_OF_RANGE);
        }

        @Test
        void isSuccess() {
            saveRequest.setTerritoryId("Terr1");
            saveRequest.setTerritoryDescription("First Territory");

            Region region = new Region();
            region.setRegionId(1L);

            when(regionService.getRegion(1L)).thenReturn(region);
            when(territoryRepository.save(any(Territory.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = territorySrv.add(saveRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.SUCCESS);
        }
    }

    @Nested
    class update {

        @Test
        void isSuccess() {
            when(territoryRepository.existsTerritoryByTerritoryId(updateRequest.getTerritoryId())).thenReturn(true);

            when(territoryRepository.save(any(Territory.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = territorySrv.update(updateRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_UPDATED);
        }
    }

    @Nested
    class findTerritoryById {
        @Test
        void isTerritoryNotFound() {
            when(territoryRepository.findTerritoryByTerritoryId("Terr1")).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> territorySrv.findTerritoryByTerritoryId("Terr1")
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.TERRITORY_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            Territory territory = new Territory();
            territory.setTerritoryId("Terr1");
            territory.setTerritoryDescription("First Territory");

            when(territoryRepository.findTerritoryByTerritoryId(territory.getTerritoryId())).thenReturn(Optional.of(territory));

            DataGenericResponse<TerritoryDto> response = territorySrv.findTerritoryByTerritoryId(territory.getTerritoryId());

            assertThat(response).isNotNull();
            assertThat(response.getData().getTerritoryDescription()).isEqualTo("First Territory");
        }
    }

    @Nested
    class delete {

        @Test
        void isTerritoryNotFound() {
            when(territoryRepository.existsTerritoryByTerritoryId("Terr1")).thenReturn(false);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> territorySrv.deleteTerritoryByTerritoryId("Terr1")
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.RECORD_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            when(territoryRepository.existsTerritoryByTerritoryId("Terr1")).thenReturn(true);

            GenericResponse response = territorySrv.deleteTerritoryByTerritoryId("Terr1");

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_DELETED);
        }
    }

    @Nested
    class findAllTerritories {

        @Test
        void isSuccess() {
            Territory terr1 = new Territory();
            terr1.setTerritoryId("Terr1");
            terr1.setTerritoryDescription("First Territory");

            Territory terr2 = new Territory();
            terr2.setTerritoryId("Terr2");
            terr2.setTerritoryDescription("Second Territory");

            when(territoryRepository.findAll()).thenReturn(List.of(terr1, terr2));

            DataGenericResponse<List<TerritoryDto>>  response = territorySrv.findAllTerritories();

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getData().size()).isEqualTo(2);
        }
    }

}