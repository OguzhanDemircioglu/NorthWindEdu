package com.server.app.service.srvImpl;

import com.server.app.dto.request.employeeTerritory.EmployeeTerritorySaveRequest;
import com.server.app.dto.request.employeeTerritory.EmployeeTerritoryUpdateRequest;
import com.server.app.dto.response.EmployeeTerritoryDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.EmployeeTerritoryMapper;
import com.server.app.model.Employee;
import com.server.app.model.EmployeeTerritory;
import com.server.app.model.Territory;
import com.server.app.model.embedded.EmployeeTerritoryId;
import com.server.app.repository.EmployeeTerritoryRepository;
import com.server.app.service.EmployeeService;
import com.server.app.service.TerritoryService;
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
class EmployeeTerritorySrvImplTest {

    @Mock
    private EmployeeTerritoryRepository employeeTerritoryRepository;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private TerritoryService territoryService;

    @InjectMocks
    private EmployeeTerritoryMapper employeeTerritoryMapper;

    @InjectMocks
    private EmployeeTerritorySrvImpl employeeTerritorySrv;

    EmployeeTerritorySaveRequest saveRequest = new EmployeeTerritorySaveRequest();
    EmployeeTerritoryUpdateRequest updateRequest = new EmployeeTerritoryUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setTerritoryId(1L);
        saveRequest.setEmployeeId(1L);

        updateRequest.setTerritoryId(2L);
        updateRequest.setEmployeeId(2L);

        employeeTerritoryMapper = new EmployeeTerritoryMapper(employeeTerritoryRepository,  employeeService, territoryService);
        employeeTerritorySrv = new EmployeeTerritorySrvImpl(employeeTerritoryRepository, employeeTerritoryMapper);
    }

    @AfterEach
    void tearDown() {
        saveRequest = null;
        updateRequest = null;
        employeeTerritoryMapper = null;
        employeeTerritorySrv = null;
    }

    @Nested
    class add {

        @Test
        void isEmptyEmployeeId() {
            saveRequest.setEmployeeId(null);

            when(territoryService.getTerritory(saveRequest.getTerritoryId())).thenReturn(new Territory());
            when(employeeService.getEmployee(saveRequest.getEmployeeId())).thenReturn(new Employee());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> employeeTerritorySrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        @Test
        void isEmptyTerritoryId() {
            saveRequest.setTerritoryId(null);

            when(territoryService.getTerritory(saveRequest.getTerritoryId())).thenReturn(new Territory());
            when(employeeService.getEmployee(saveRequest.getEmployeeId())).thenReturn(new Employee());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> employeeTerritorySrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.EMPTY_TERRITORY_ID);
        }

        @Test
        void isSuccess() {
            when(employeeService.getEmployee(saveRequest.getEmployeeId())).thenReturn(new Employee());
            when(territoryService.getTerritory(saveRequest.getTerritoryId())).thenReturn(new Territory());
            when(employeeTerritoryRepository.save(any(EmployeeTerritory.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = employeeTerritorySrv.add(saveRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.SUCCESS);
        }
    }

    @Nested
    class update {

        @Test
        void isEmployeeTerritoryNotFound() {
            EmployeeTerritoryId employeeTerritoryId = new EmployeeTerritoryId(1L, 1L);

            when(employeeTerritoryRepository.findEmployeeTerritoryByEmployeeTerritoryId(employeeTerritoryId)).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> employeeTerritorySrv.findEmployeeTerritoryByEmployeeTerritoryId(employeeTerritoryId)
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.RECORD_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            when(employeeTerritoryRepository.existsByEmployeeTerritoryId_EmployeeIdAndEmployeeTerritoryId_TerritoryId(updateRequest.getEmployeeId(), updateRequest.getTerritoryId())).thenReturn(true);

            when(employeeService.getEmployee(updateRequest.getEmployeeId())).thenReturn(new Employee());
            when(territoryService.getTerritory(updateRequest.getTerritoryId())).thenReturn(new Territory());

            when(employeeTerritoryRepository.save(any(EmployeeTerritory.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = employeeTerritorySrv.update(updateRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_UPDATED);
        }
    }

    @Nested
    class findEmployeeTerritoryById {

        @Test
        void isSuccess() {
            Employee employee = new Employee();
            employee.setEmployeeId(1L);

            Territory territory = new Territory();
            territory.setTerritoryId(1L);

            EmployeeTerritoryId employeeTerritoryId = new EmployeeTerritoryId(1L, 1L);

            EmployeeTerritory employeeTerritory = new EmployeeTerritory();
            employeeTerritory.setEmployeeTerritoryId(employeeTerritoryId);
            employeeTerritory.setTerritory(territory);
            employeeTerritory.setEmployee(employee);

            when(employeeTerritoryRepository.findEmployeeTerritoryByEmployeeTerritoryId(employeeTerritoryId)).thenReturn(Optional.of(employeeTerritory));

            DataGenericResponse<EmployeeTerritoryDto> response = employeeTerritorySrv.findEmployeeTerritoryByEmployeeTerritoryId(employeeTerritoryId);

            assertThat(response).isNotNull();
            assertThat(response.getData().getEmployeeId()).isEqualTo(1L);
            assertThat(response.getData().getTerritoryId()).isEqualTo(1L);
        }
    }

    @Nested
    class delete {

        @Test
        void isEmployeeTerritoryNotFound() {
            EmployeeTerritoryId employeeTerritoryId = new EmployeeTerritoryId(1L, 1L);

            when(employeeTerritoryRepository.existsByEmployeeTerritoryId_EmployeeIdAndEmployeeTerritoryId_TerritoryId(1L, 1L)).thenReturn(false);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> employeeTerritorySrv.deleteEmployeeTerritoryByEmployeeTerritoryId(employeeTerritoryId)
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.RECORD_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            EmployeeTerritoryId employeeTerritoryId = new EmployeeTerritoryId(1L, 1L);

            when(employeeTerritoryRepository.existsByEmployeeTerritoryId_EmployeeIdAndEmployeeTerritoryId_TerritoryId(1L, 1L)).thenReturn(true);

            GenericResponse response = employeeTerritorySrv.deleteEmployeeTerritoryByEmployeeTerritoryId(employeeTerritoryId);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_DELETED);
        }
    }

    @Nested
    class findAllEmployeeTerritories {

        @Test
        void isSuccess() {
            EmployeeTerritory employeeTerritory1 = new EmployeeTerritory();
            EmployeeTerritory employeeTerritory2 = new EmployeeTerritory();

            when(employeeTerritoryRepository.findAll()).thenReturn(List.of(employeeTerritory1, employeeTerritory2));

            DataGenericResponse<List<EmployeeTerritoryDto>> response = employeeTerritorySrv.findAllEmployeeTerritories();

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getData().size()).isEqualTo(2);
        }
    }
}