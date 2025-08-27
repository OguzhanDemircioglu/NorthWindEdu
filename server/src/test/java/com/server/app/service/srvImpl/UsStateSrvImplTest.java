package com.server.app.service.srvImpl;

import com.server.app.dto.request.usState.UsStateSaveRequest;
import com.server.app.dto.request.usState.UsStateUpdateRequest;
import com.server.app.dto.response.UsStateDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.UsStateMapper;
import com.server.app.model.UsState;
import com.server.app.repository.UsStateRepository;
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
class UsStateSrvImplTest {

    @Mock
    private UsStateRepository stateRepository;

    @InjectMocks
    private UsStateMapper stateMapper;

    @InjectMocks
    private UsStateSrvImpl stateSrv;

    UsStateSaveRequest saveRequest = new UsStateSaveRequest();
    UsStateUpdateRequest updateRequest = new UsStateUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setStateName("First State");
        saveRequest.setStateAbbr("S1");
        saveRequest.setStateRegion("First Region");

        updateRequest.setStateName("Second State");
        updateRequest.setStateAbbr("S2");
        updateRequest.setStateRegion("Second Region");
        updateRequest.setStateId(1L);

        stateMapper = new UsStateMapper(stateRepository);
        stateSrv = new UsStateSrvImpl(stateRepository, stateMapper);
    }

    @AfterEach
    void tearDown() {
        saveRequest = null;
        updateRequest = null;
        stateMapper = null;
        stateSrv = null;
    }

    @Nested
    class add {

        @Test
        void isInvalidName() {
            saveRequest.setStateName("s".repeat(101));

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> stateSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.STATE_NAME_OUT_OF_RANGE);
        }

        @Test
        void isInvalidRegion() {
            saveRequest.setStateRegion("s".repeat(51));

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> stateSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.STATE_REGION_OUT_OF_RANGE);
        }

        @Test
        void isInvalidAbbr() {
            saveRequest.setStateAbbr("s".repeat(3));

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> stateSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.STATE_ABBR_OUT_OF_RANGE);
        }

        @Test
        void isSuccess() {
            saveRequest.setStateName("First State");
            saveRequest.setStateAbbr("S1");
            saveRequest.setStateRegion("First Region");

            when(stateRepository.save(any(UsState.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = stateSrv.add(saveRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.SUCCESS);
        }
    }

    @Nested
    class update {
        @Test
        void isEmptyId() {
            updateRequest.setStateId(null);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> stateSrv.update(updateRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        @Test
        void isSuccess() {
            when(stateRepository.existsStateByStateId(updateRequest.getStateId())).thenReturn(true);

            when(stateRepository.save(any(UsState.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = stateSrv.update(updateRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_UPDATED);
        }
    }

    @Nested
    class findStateById {

        @Test
        void isStateNotFound() {
            when(stateRepository.findStateByStateId(1L)).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> stateSrv.findStateByStateId(1L)
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.STATE_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            UsState state = new UsState();
            state.setStateId(1L);
            state.setStateName("First State");

            when(stateRepository.findStateByStateId(state.getStateId())).thenReturn(Optional.of(state));

            DataGenericResponse<UsStateDto> response = stateSrv.findStateByStateId(state.getStateId());

            assertThat(response).isNotNull();
            assertThat(response.getData().getStateName()).isEqualTo("First State");
        }
    }

    @Nested
    class delete {

        @Test
        void isStateNotFound() {
            when(stateRepository.existsStateByStateId(1L)).thenReturn(false);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> stateSrv.deleteStateByStateId(1L)
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.RECORD_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            when(stateRepository.existsStateByStateId(1L)).thenReturn(true);

            GenericResponse response = stateSrv.deleteStateByStateId(1L);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_DELETED);
        }
    }

    @Nested
    class findAllStates {

        @Test
        void isSuccess() {
            UsState state1 = new UsState();
            state1.setStateId(1L);
            state1.setStateName("First State");

            UsState state2 = new UsState();
            state2.setStateId(2L);
            state2.setStateName("Second State");

            when(stateRepository.findAll()).thenReturn(List.of(state1, state2));

            DataGenericResponse<List<UsStateDto>> response = stateSrv.findAllStates();

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getData().size()).isEqualTo(2);
        }
    }
}