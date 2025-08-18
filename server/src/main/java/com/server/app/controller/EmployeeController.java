package com.server.app.controller;

import com.server.app.dto.response.EmployeeDto;
import com.server.app.dto.request.employee.EmployeeSaveRequest;
import com.server.app.dto.request.employee.EmployeeUpdateRequest;
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
    public ResponseEntity<?> add(@RequestBody EmployeeSaveRequest request){
        String resultMessage = employeeService.add(request);
        return ResponseEntity.ok(resultMessage);
    }

    @PutMapping("/update")
    public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody EmployeeUpdateRequest request){
        EmployeeDto updatedEmployee = employeeService.update(request);
        return ResponseEntity.ok(updatedEmployee);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> findEmployeeByEmployeeId(@PathVariable Long id){
        EmployeeDto employee = employeeService.findEmployeeByEmployeeId(id);
        return ResponseEntity.ok(employee);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        employeeService.deleteEmployeeByEmployeeId(id);
        return ResponseEntity.ok("İşlem Başarılı");
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> findAllEmployees(){
        return ResponseEntity.ok(employeeService.findAllEmployees());
    }
}
