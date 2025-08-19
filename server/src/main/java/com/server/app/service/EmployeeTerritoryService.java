package com.server.app.service;

import com.server.app.dto.request.employeeTerritory.EmployeeTerritorySaveRequest;
import com.server.app.dto.request.employeeTerritory.EmployeeTerritoryUpdateRequest;
import com.server.app.dto.response.EmployeeTerritoryDto;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.embedded.EmployeeTerritoryId;

import java.util.List;

public interface EmployeeTerritoryService {

    GenericResponse add(EmployeeTerritorySaveRequest request);

    GenericResponse update(EmployeeTerritoryUpdateRequest request);

    DataGenericResponse<EmployeeTerritoryDto> findEmployeeTerritoryByEmployeeTerritoryId(EmployeeTerritoryId id);

    GenericResponse deleteEmployeeTerritoryByEmployeeTerritoryId(EmployeeTerritoryId id);

    DataGenericResponse<List<EmployeeTerritoryDto>> findAllEmployeeTerritories();
}
