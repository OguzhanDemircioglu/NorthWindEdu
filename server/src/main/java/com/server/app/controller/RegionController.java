package com.server.app.controller;

import com.server.app.dto.response.RegionDto;
import com.server.app.dto.request.region.RegionSaveRequest;
import com.server.app.dto.request.region.RegionUpdateRequest;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.RegionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @PostMapping("/add")
    public ResponseEntity<GenericResponse> add(@RequestBody RegionSaveRequest request) {
        return ResponseEntity.ok(regionService.add(request));
    }

    @PutMapping("/update")
    public ResponseEntity<GenericResponse> update(@RequestBody RegionUpdateRequest request) {
        return ResponseEntity.ok(regionService.update(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataGenericResponse<RegionDto>> getRegionByRegionId(@PathVariable Long id) {
        DataGenericResponse<RegionDto> result = regionService.findRegionByRegionId(id);
        return ResponseEntity.ok(result);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteRegionByRegionId(@PathVariable Long id) {
        GenericResponse result = regionService.deleteRegionByRegionId(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<DataGenericResponse<List<RegionDto>>> findAllRegions() {
        DataGenericResponse<List<RegionDto>> result = regionService.findAllRegions();
        return ResponseEntity.ok(result);
    }
}
