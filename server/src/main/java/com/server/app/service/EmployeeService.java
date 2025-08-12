package com.server.app.service;

import com.server.app.dto.EmployeeDto;
import com.server.app.dto.request.EmployeeSaveRequest;
import com.server.app.dto.request.EmployeeUpdateRequest;

import java.util.List;

public interface EmployeeService {

    String add(EmployeeSaveRequest request);

    EmployeeDto update(EmployeeUpdateRequest request);

    EmployeeDto findEmployeeByEmployeeId(Integer employeeId);

    void deleteEmployeeByEmployeeId(Integer employeeId);

    List<EmployeeDto> findAllEmployees();
}
