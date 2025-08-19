package com.server.app.service.srvImpl;

import com.google.common.base.Strings;
import com.server.app.dto.request.employeeTerritory.EmployeeTerritorySaveRequest;
import com.server.app.dto.request.employeeTerritory.EmployeeTerritoryUpdateRequest;
import com.server.app.dto.response.EmployeeTerritoryDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.EmployeeTerritoryMapper;
import com.server.app.model.EmployeeTerritory;
import com.server.app.model.embedded.EmployeeTerritoryId;
import com.server.app.repository.EmployeeTerritoryRepository;
import com.server.app.service.EmployeeTerritoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeTerritorySrvImpl implements EmployeeTerritoryService {

    private final EmployeeTerritoryRepository employeeTerritoryRepository;
    private final EmployeeTerritoryMapper mapper;

    @Override
    public GenericResponse add(EmployeeTerritorySaveRequest request) {
        EmployeeTerritory employeeTerritory = mapper.saveEntityFromRequest(request);

        BusinessRules.validate(
                checkEmployeeTerritoryForGeneralValidations(employeeTerritory)
        );
        employeeTerritoryRepository.save(employeeTerritory);
        return new GenericResponse();
    }

    @Override
    public GenericResponse update(EmployeeTerritoryUpdateRequest request) {
        EmployeeTerritory employeeTerritory = mapper.toEntity(request);

        BusinessRules.validate(
                checkEmployeeTerritoryForGeneralValidations(employeeTerritory)
        );

        employeeTerritoryRepository.save(employeeTerritory);
        return GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).build();
    }

    @Override
    public DataGenericResponse<EmployeeTerritoryDto> findEmployeeTerritoryByEmployeeTerritoryId(EmployeeTerritoryId id) {
        Optional<EmployeeTerritory> employeeTerritory = employeeTerritoryRepository.findEmployeeTerritoryByEmployeeTerritoryId(id);
        if (employeeTerritory.isEmpty()) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }
        EmployeeTerritoryDto dto = mapper.toDto(employeeTerritory.get());

        return DataGenericResponse.<EmployeeTerritoryDto>dataBuilder()
                .data(dto)
                .build();
    }

    @Override
    public GenericResponse deleteEmployeeTerritoryByEmployeeTerritoryId(EmployeeTerritoryId id) {
        boolean isExists = employeeTerritoryRepository.existsByEmployeeTerritoryId_EmployeeIdAndEmployeeTerritoryId_TerritoryId(id.getEmployeeId(), id.getTerritoryId());
        if (!isExists) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }
        employeeTerritoryRepository.deleteEmployeeTerritoryByEmployeeTerritoryId(id);

        return GenericResponse.builder().message(ResultMessages.RECORD_DELETED).build();
    }

    @Override
    public DataGenericResponse<List<EmployeeTerritoryDto>> findAllEmployeeTerritories() {
        List<EmployeeTerritory> employeeTerritories = employeeTerritoryRepository.findAll();
        List<EmployeeTerritoryDto> dtos = employeeTerritories.stream()
                .map(mapper::toDto)
                .toList();

        return DataGenericResponse.<List<EmployeeTerritoryDto>>dataBuilder()
                .data(dtos)
                .build();
    }

    private String checkEmployeeTerritoryForGeneralValidations(EmployeeTerritory request) {
        if(request.getEmployeeTerritoryId().getEmployeeId() == null || request.getEmployeeTerritoryId().getEmployeeId() == 0) {
            return ResultMessages.ID_IS_NOT_DELIVERED;
        }

        if(Strings.isNullOrEmpty(request.getEmployeeTerritoryId().getTerritoryId())) {
            return ResultMessages.EMPTY_TERRITORY_ID;
        }
        return null;
    }
}
