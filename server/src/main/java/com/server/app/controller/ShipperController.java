package com.server.app.controller;

import com.server.app.dto.ShipperDto;
import com.server.app.dto.request.ShipperSaveRequest;
import com.server.app.dto.request.ShipperUpdateRequest;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.ShipperService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shippers")
@RequiredArgsConstructor
public class ShipperController {

    private final ShipperService shipperService;

    @PostMapping("/add")
    public ResponseEntity<GenericResponse> add(@RequestBody ShipperSaveRequest request) {
        return ResponseEntity.ok(shipperService.add(request));
    }

    @PutMapping("/update")
    public ResponseEntity<GenericResponse> update(@RequestBody ShipperUpdateRequest request) {
        return ResponseEntity.ok(shipperService.update(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataGenericResponse<ShipperDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(shipperService.findShipperByShipperId(id));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(shipperService.deleteShipperByShipperId(id));
    }

    @GetMapping
    public ResponseEntity<DataGenericResponse<List<ShipperDto>>> findAll() {
        return ResponseEntity.ok(shipperService.findAllShippers());
    }
}
