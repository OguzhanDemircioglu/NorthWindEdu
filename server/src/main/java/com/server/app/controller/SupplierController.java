package com.server.app.controller;

import com.server.app.dto.response.SupplierDto;
import com.server.app.dto.request.supplier.SupplierSaveRequest;
import com.server.app.dto.request.supplier.SupplierUpdateRequest;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.SupplierService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping("/add")
    public ResponseEntity<GenericResponse> add(@RequestBody SupplierSaveRequest request){
        return ResponseEntity.ok(supplierService.add(request));
    }

    @PutMapping("/update")
    public ResponseEntity<GenericResponse> update(@RequestBody SupplierUpdateRequest request){
        return ResponseEntity.ok(supplierService.update(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataGenericResponse<SupplierDto>> get(@PathVariable Long id){
        DataGenericResponse<SupplierDto> result = supplierService.findSupplierBySupplierId(id);
        return ResponseEntity.ok(result);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> delete(@PathVariable Long id){
        GenericResponse result = supplierService.deleteSupplierBySupplierId(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<DataGenericResponse<List<SupplierDto>>> findAllSuppliers(){
        DataGenericResponse<List<SupplierDto>> result = supplierService.findAllSuppliers();
        return ResponseEntity.ok(result);
    }
}
