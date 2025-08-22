package com.server.app.service;

import com.server.app.dto.request.employee.EmployeeSaveRequest;
import com.server.app.dto.request.employee.EmployeeUpdateRequest;
import com.server.app.dto.response.EmployeeDto;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.Employee;

import java.util.List;

public interface EmployeeService {

    GenericResponse add(EmployeeSaveRequest request);

    GenericResponse update(EmployeeUpdateRequest request);

    DataGenericResponse<EmployeeDto> findEmployeeByEmployeeId(Long employeeId);

    GenericResponse deleteEmployeeByEmployeeId(Long employeeId);

    DataGenericResponse<List<EmployeeDto>> findAllEmployees();

    Employee getEmployee(Long employeeId);
}
