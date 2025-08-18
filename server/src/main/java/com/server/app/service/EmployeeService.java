package com.server.app.service;

import com.server.app.dto.response.EmployeeDto;
import com.server.app.dto.request.employee.EmployeeSaveRequest;
import com.server.app.dto.request.employee.EmployeeUpdateRequest;

import java.util.List;

public interface EmployeeService {

    String add(EmployeeSaveRequest request);

    EmployeeDto update(EmployeeUpdateRequest request);

    EmployeeDto findEmployeeByEmployeeId(Long employeeId);

    void deleteEmployeeByEmployeeId(Long employeeId);

    List<EmployeeDto> findAllEmployees();
}
