package com.server.app.controller;

import com.server.app.dto.response.EmployeeDto;
import com.server.app.dto.request.employee.EmployeeSaveRequest;
import com.server.app.dto.request.employee.EmployeeUpdateRequest;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/add")
    public ResponseEntity<GenericResponse> add(@RequestBody EmployeeSaveRequest request){
        return ResponseEntity.ok(employeeService.add(request));
    }

    @PutMapping("/update")
    public ResponseEntity<GenericResponse> update(@RequestBody EmployeeUpdateRequest request){
        return ResponseEntity.ok(employeeService.update(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataGenericResponse<EmployeeDto>> FindEmployeeById(@PathVariable Long id){
        DataGenericResponse<EmployeeDto> result = employeeService.findEmployeeByEmployeeId(id);
        return ResponseEntity.ok(result);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteEmployeeById(@PathVariable Long id){
        GenericResponse result = employeeService.deleteEmployeeByEmployeeId(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<DataGenericResponse<List<EmployeeDto>>> findAllEmployees(){
        DataGenericResponse<List<EmployeeDto>> result = employeeService.findAllEmployees();
        return ResponseEntity.ok(result);
    }
}
