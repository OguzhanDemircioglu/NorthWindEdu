package com.server.app.mapper;

import com.server.app.dto.request.employeeTerritory.EmployeeTerritorySaveRequest;
import com.server.app.dto.request.employeeTerritory.EmployeeTerritoryUpdateRequest;
import com.server.app.dto.response.EmployeeTerritoryDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.model.Employee;
import com.server.app.model.EmployeeTerritory;
import com.server.app.model.Territory;
import com.server.app.model.embedded.EmployeeTerritoryId;
import com.server.app.repository.EmployeeTerritoryRepository;
import com.server.app.service.EmployeeService;
import com.server.app.service.TerritoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EmployeeTerritoryMapper {
    private final EmployeeTerritoryRepository employeeTerritoryRepository;
    private final EmployeeService employeeService;
    private final TerritoryService territoryService;

    public EmployeeTerritoryDto toDto(EmployeeTerritory request) {
        return EmployeeTerritoryDto.builder()
                .employeeId(
                        Objects.isNull(request.getEmployee())
                                ? null
                                : request.getEmployee().getEmployeeId()
                )
                .territoryId(
                        Objects.isNull(request.getTerritory())
                        ? null
                        : request.getTerritory().getTerritoryId()
                )
                .build();
    }

    public EmployeeTerritory toEntity(EmployeeTerritoryUpdateRequest request) {
        if (request.getEmployeeId() == null || request.getEmployeeId() == 0 ||
                request.getTerritoryId() == null || request.getTerritoryId().isEmpty()) {
            throw new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        boolean isExist = employeeTerritoryRepository.existsByEmployeeTerritoryId_EmployeeIdAndEmployeeTerritoryId_TerritoryId(request.getEmployeeId(), request.getTerritoryId());
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        Employee employee = employeeService.getEmployee(request.getEmployeeId());
        if (Objects.isNull(employee)) {
            throw new BusinessException(ResultMessages.EMPLOYEE_NOT_FOUND);
        }

        Territory territory = territoryService.getTerritory(request.getTerritoryId());
        if (Objects.isNull(territory)) {
            throw new BusinessException(ResultMessages.TERRITORY_NOT_FOUND);
        }

        return updateEntityFromRequest(request, employee, territory);
    }

    private EmployeeTerritory updateEntityFromRequest(EmployeeTerritoryUpdateRequest request, Employee employee, Territory territory) {
        return EmployeeTerritory.builder()
                .employeeTerritoryId(new EmployeeTerritoryId(request.getEmployeeId(), request.getTerritoryId()))
                .employee(employee)
                .territory(territory)
                .build();
    }

    public EmployeeTerritory saveEntityFromRequest(EmployeeTerritorySaveRequest request) {
        Employee employee = employeeService.getEmployee(request.getEmployeeId());
        if (Objects.isNull(employee)) {
            throw new BusinessException(ResultMessages.EMPLOYEE_NOT_FOUND);
        }

        Territory territory = territoryService.getTerritory(request.getTerritoryId());
        if (Objects.isNull(territory)) {
            throw new BusinessException(ResultMessages.TERRITORY_NOT_FOUND);
        }

        return EmployeeTerritory.builder()
                .employeeTerritoryId(new EmployeeTerritoryId(request.getEmployeeId(), request.getTerritoryId()))
                .employee(employee)
                .territory(territory)
                .build();
    }
}
