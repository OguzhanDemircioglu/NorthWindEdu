package com.server.app.controller;

import com.server.app.dto.request.employeeTerritory.EmployeeTerritorySaveRequest;
import com.server.app.dto.request.employeeTerritory.EmployeeTerritoryUpdateRequest;
import com.server.app.dto.response.EmployeeTerritoryDto;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.embedded.EmployeeTerritoryId;
import com.server.app.service.EmployeeTerritoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-territories")
@RequiredArgsConstructor
public class EmployeeTerritoryController {

    private final EmployeeTerritoryService employeeTerritoryService;

    @PostMapping("/add")
    public ResponseEntity<GenericResponse> add(@RequestBody EmployeeTerritorySaveRequest request){
        return ResponseEntity.ok(employeeTerritoryService.add(request));
    }

    @PutMapping("/update")
    public ResponseEntity<GenericResponse> update(@RequestBody EmployeeTerritoryUpdateRequest request){
        return ResponseEntity.ok(employeeTerritoryService.update(request));
    }

    @GetMapping("/")
    public ResponseEntity<DataGenericResponse<EmployeeTerritoryDto>> get(
            @RequestParam Long employeeId,
            @RequestParam String territoryId) {

        EmployeeTerritoryId id = new EmployeeTerritoryId(employeeId, territoryId);

        DataGenericResponse<EmployeeTerritoryDto> result = employeeTerritoryService.findEmployeeTerritoryByEmployeeTerritoryId(id);
        return ResponseEntity.ok(result);
    }

    @Transactional
    @DeleteMapping("/")
    public ResponseEntity<GenericResponse> delete(
            @RequestParam Long employeeId,
            @RequestParam String territoryId) {
        EmployeeTerritoryId id = new EmployeeTerritoryId(employeeId, territoryId);
        GenericResponse result = employeeTerritoryService.deleteEmployeeTerritoryByEmployeeTerritoryId(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<DataGenericResponse<List<EmployeeTerritoryDto>>> findAllEmployeeTerritories() {
        DataGenericResponse<List<EmployeeTerritoryDto>> result = employeeTerritoryService.findAllEmployeeTerritories();
        return ResponseEntity.ok(result);
    }
}
