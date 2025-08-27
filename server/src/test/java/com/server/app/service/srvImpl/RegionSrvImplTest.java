package com.server.app.service.srvImpl;

import com.server.app.dto.request.region.RegionSaveRequest;
import com.server.app.dto.request.region.RegionUpdateRequest;
import com.server.app.dto.response.RegionDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.RegionMapper;
import com.server.app.model.Region;
import com.server.app.repository.RegionRepository;
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
class RegionSrvImplTest {

    @Mock
    private RegionRepository  regionRepository;

    @InjectMocks
    private RegionMapper regionMapper;

    @InjectMocks
    private RegionSrvImpl regionSrv;

    RegionSaveRequest saveRequest = new RegionSaveRequest();
    RegionUpdateRequest updateRequest = new RegionUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setRegionDescription("ilk bölge");

        updateRequest.setRegionDescription("güncel bölge");
        updateRequest.setRegionId(1L);

        regionMapper = new RegionMapper(regionRepository);
        regionSrv = new RegionSrvImpl(regionRepository, regionMapper);
    }

    @AfterEach
    void tearDown() {
        saveRequest = null;
        updateRequest = null;
        regionMapper = null;
        regionSrv = null;
    }

    @Nested
    class add {

        @Test
        void isEmptyDescription() {
            saveRequest.setRegionDescription("");

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> regionSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.EMPTY_DESCRIPTION);
        }

        @Test
        void isSuccess() {
            saveRequest.setRegionDescription("ilk bölge");

            when(regionRepository.save(any(Region.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = regionSrv.add(saveRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.SUCCESS);
        }
    }

    @Nested
    class update {

        @Test
        void isEmptyId() {
            updateRequest.setRegionId(null);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> regionSrv.update(updateRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        @Test
        void isSuccess() {
            when(regionRepository.existsById(updateRequest.getRegionId())).thenReturn(true);

            when(regionRepository.save(any(Region.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = regionSrv.update(updateRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_UPDATED);
        }
    }

    @Nested
    class findRegionById {

        @Test
        void isRegionNotFound() {
            when(regionRepository.findRegionByRegionId(1L)).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> regionSrv.findRegionByRegionId(1L)
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.REGION_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            Region region = new Region();
            region.setRegionId(1L);
            region.setRegionDescription("ilk bölge");

            when(regionRepository.findRegionByRegionId(1L)).thenReturn(Optional.of(region));

            DataGenericResponse<RegionDto> response = regionSrv.findRegionByRegionId(1L);

            assertThat(response).isNotNull();
            assertThat(response.getData().getRegionDescription()).isEqualTo("ilk bölge");
        }
    }

    @Nested
    class delete {

        @Test
        void isRegionNotFound() {
            when(regionRepository.existsRegionByRegionId(1L)).thenReturn(false);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> regionSrv.deleteRegionByRegionId(1L)
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.RECORD_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            when(regionRepository.existsRegionByRegionId(1L)).thenReturn(true);

            GenericResponse response = regionSrv.deleteRegionByRegionId(1L);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_DELETED);
        }
    }

    @Nested
    class findAllRegions {

        @Test
        void isSuccess() {
            Region reg1 = new Region();
            reg1.setRegionId(1L);
            reg1.setRegionDescription("ilk bölge");

            Region reg2 = new Region();
            reg2.setRegionId(2L);
            reg2.setRegionDescription("ikinci bölge");

            when(regionRepository.findAll()).thenReturn(List.of(reg1, reg2));

            DataGenericResponse<List<RegionDto>> response = regionSrv.findAllRegions();

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getData().size()).isEqualTo(2);

        }
    }

    @Nested
    class getRegion {

        @Test
        void isRegionNotFound() {
            when(regionRepository.getRegionByRegionId(1L)).thenReturn(null);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> regionSrv.getRegion(1L)
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.REGION_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            Region region = new Region();
            region.setRegionId(1L);
            region.setRegionDescription("ilk bolge");

            when(regionRepository.getRegionByRegionId(1L)).thenReturn(region);

            Region result = regionSrv.getRegion(1L);

            assertThat(result).isNotNull();
            assertThat(result.getRegionDescription()).isEqualTo("ilk bolge");
        }
    }

}