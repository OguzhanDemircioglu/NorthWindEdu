package com.server.app.controller;


import com.server.app.dto.request.territory.TerritorySaveRequest;
import com.server.app.dto.request.territory.TerritoryUpdateRequest;
import com.server.app.dto.response.TerritoryDto;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.TerritoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/territories")
@RequiredArgsConstructor
public class TerritoryController {

    private final TerritoryService territoryService;

    @PostMapping("/add")
    public ResponseEntity<GenericResponse> add(@RequestBody TerritorySaveRequest request) {
        return ResponseEntity.ok(territoryService.add(request));
    }

    @PutMapping("/update")
    public ResponseEntity<GenericResponse> update(@RequestBody TerritoryUpdateRequest request) {
        return ResponseEntity.ok(territoryService.update(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataGenericResponse<TerritoryDto>> get(@PathVariable String id) {
        DataGenericResponse<TerritoryDto> result = territoryService.findTerritoryByTerritoryId(id);
        return ResponseEntity.ok(result);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> delete(@PathVariable String id) {
        GenericResponse result = territoryService.deleteTerritoryByTerritoryId(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<DataGenericResponse<List<TerritoryDto>>> findAllTerritories() {
        DataGenericResponse<List<TerritoryDto>> result = territoryService.findAllTerritories();
        return ResponseEntity.ok(result);
    }
}
