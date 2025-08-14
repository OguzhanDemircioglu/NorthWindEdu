package com.server.app.controller;

import com.server.app.dto.RegionDto;
import com.server.app.dto.request.RegionSaveRequest;
import com.server.app.dto.request.RegionUpdateRequest;
import com.server.app.service.RegionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/region")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody RegionSaveRequest request) {
        String resultMessage = regionService.add(request);
        return ResponseEntity.ok(resultMessage);
    }

    @PutMapping("/update")
    public ResponseEntity<RegionDto> update(@RequestBody RegionUpdateRequest request) {
        RegionDto updatedRegion = regionService.update(request);
        return ResponseEntity.ok(updatedRegion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDto> getRegionByRegionId(@PathVariable Short id) {
        RegionDto region = regionService.findRegionByRegionId(id);
        return ResponseEntity.ok(region);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRegionByRegionId(@PathVariable Short id) {
        regionService.deleteRegionByRegionId(id);
        return ResponseEntity.ok("İşlem Başarılı");
    }

    @GetMapping
    public ResponseEntity<List<RegionDto>> findAllRegions() {
        return ResponseEntity.ok(regionService.findAllRegions());
    }
}
