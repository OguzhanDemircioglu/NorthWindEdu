package com.server.app.controller;

import com.server.app.dto.TerritoryDto;
import com.server.app.dto.request.TerritorySaveRequest;
import com.server.app.dto.request.TerritoryUpdateRequest;
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
    public ResponseEntity<?> add(@RequestBody TerritorySaveRequest request) {
        String resultMessage = territoryService.add(request);
        return ResponseEntity.ok(resultMessage);
    }

    @PutMapping("/update")
    public ResponseEntity<TerritoryDto> update(@RequestBody TerritoryUpdateRequest request) {
        TerritoryDto updatedTerritory = territoryService.update(request);
        return ResponseEntity.ok(updatedTerritory);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TerritoryDto> findById(@PathVariable String id) {
        TerritoryDto territory = territoryService.findTerritoryByTerritoryId(id);
        return ResponseEntity.ok(territory);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        territoryService.deleteTerritoryByTerritoryId(id);
        return ResponseEntity.ok("İşlem Başarılı");
    }

    @GetMapping
    public ResponseEntity<List<TerritoryDto>> findAllTerritories() {
        return ResponseEntity.ok(territoryService.findAllTerritories());
    }
}
