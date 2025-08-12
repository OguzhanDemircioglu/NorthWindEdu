package com.server.app.controller;

import com.server.app.model.Employees;
import com.server.app.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    // ADD
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Employees employee) {
        try {
            Employees created = service.create(employee);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalStateException ex) { // zaten varsa
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // UPDATE
    @PutMapping("/update/{firstName}/{lastName}")
    public ResponseEntity<?> update(@PathVariable String firstName,
                                    @PathVariable String lastName,
                                    @RequestBody Employees employee) {
        try {
            Employees updated = service.update(firstName, lastName, employee);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) { // bulunamadı
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // DELETE
    @DeleteMapping("/delete/{firstName}/{lastName}")
    public ResponseEntity<?> delete(@PathVariable String firstName,
                                    @PathVariable String lastName) {
        try {
            service.delete(firstName, lastName);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) { // bulunamadı
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
