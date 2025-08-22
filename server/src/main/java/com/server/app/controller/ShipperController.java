package com.server.app.controller;

import com.server.app.dto.response.ShipperDto;
import com.server.app.dto.request.shipper.ShipperSaveRequest;
import com.server.app.dto.request.shipper.ShipperUpdateRequest;
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
    public ResponseEntity<DataGenericResponse<ShipperDto>> findShipperById(@PathVariable Long id) {
        DataGenericResponse<ShipperDto> result = shipperService.findShipperByShipperId(id);
        return ResponseEntity.ok(result);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteShipperById(@PathVariable Long id) {
        GenericResponse result = shipperService.deleteShipperByShipperId(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<DataGenericResponse<List<ShipperDto>>> findAllShippers() {
        DataGenericResponse<List<ShipperDto>> result = shipperService.findAllShippers();
        return ResponseEntity.ok(result);
    }
}
