package com.server.app.controller;

import com.server.app.dto.SupplierDto;
import com.server.app.dto.request.SupplierSaveRequest;
import com.server.app.dto.request.SupplierUpdateRequest;
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
    public ResponseEntity<?> add(@RequestBody SupplierSaveRequest request){
        String resultMessage = supplierService.add(request);
        return ResponseEntity.ok(resultMessage);
    }

    @PutMapping("/update")
    public ResponseEntity<SupplierDto> update(@RequestBody SupplierUpdateRequest request){
        SupplierDto updated = supplierService.update(request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> findSupplierBySupplierId(@PathVariable Long id){
        SupplierDto supplier = supplierService.findSupplierBySupplierId(id);
        return ResponseEntity.ok(supplier);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        supplierService.deleteSupplierBySupplierId(id);
        return ResponseEntity.ok().body("İslem Basarili");
    }

    @GetMapping
    public ResponseEntity<List<SupplierDto>> findAllSuppliers(){
        return ResponseEntity.ok(supplierService.findAllSuppliers());
    }
}
