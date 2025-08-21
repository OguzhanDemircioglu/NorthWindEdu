package com.server.app.controller;

import com.server.app.dto.response.UsStateDto;
import com.server.app.dto.request.usState.UsStateSaveRequest;
import com.server.app.dto.request.usState.UsStateUpdateRequest;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.UsStateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/us-states")
@RequiredArgsConstructor
public class UsStateController {

    private final UsStateService stateService;

    @PostMapping("/add")
    public ResponseEntity<GenericResponse> add(@RequestBody UsStateSaveRequest request) {
        return ResponseEntity.ok(stateService.add(request));
    }

    @PutMapping("/update")
    public ResponseEntity<GenericResponse> updateState(@RequestBody UsStateUpdateRequest request) {
        return ResponseEntity.ok(stateService.update(request));
    }

    @GetMapping("{id}")
    public ResponseEntity<DataGenericResponse<UsStateDto>> getState(@PathVariable Long id) {
        DataGenericResponse<UsStateDto> result = stateService.findStateByStateId(id);
        return ResponseEntity.ok(result);
    }

    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity<GenericResponse> deleteState(@PathVariable Long id) {
        GenericResponse resut = stateService.deleteStateByStateId(id);
        return ResponseEntity.ok(resut);
    }

    @GetMapping
    public ResponseEntity<DataGenericResponse<List<UsStateDto>>> findAllStates() {
        DataGenericResponse<List<UsStateDto>> result = stateService.findAllStates();
        return ResponseEntity.ok(result);
    }
}
