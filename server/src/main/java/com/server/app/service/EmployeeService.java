package com.server.app.service;

import com.server.app.dto.request.employee.EmployeeSaveRequest;
import com.server.app.dto.request.employee.EmployeeUpdateRequest;
import com.server.app.dto.response.EmployeeDto;
import com.server.app.model.Employee;

import java.util.List;

public interface EmployeeService {

    // CRUD (DTO tabanlı)
    String add(EmployeeSaveRequest request);

    EmployeeDto update(EmployeeUpdateRequest request);

    EmployeeDto findEmployeeByEmployeeId(Long employeeId);

    void deleteEmployeeByEmployeeId(Long employeeId);

    boolean existsByEmployeeId(Long employeeId);

    List<EmployeeDto> findAllEmployees();

    // OrderMapper'ın ihtiyaç duyduğu: ENTITY döndürür
    Employee getEmployee(Long employeeId);
}
