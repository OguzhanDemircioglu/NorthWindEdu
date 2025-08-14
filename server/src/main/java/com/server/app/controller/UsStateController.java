package com.server.app.controller;

import com.server.app.dto.UsStateDto;
import com.server.app.dto.request.UsStateSaveRequest;
import com.server.app.dto.request.UsStateUpdateRequest;
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
    public ResponseEntity<?> add(@RequestBody UsStateSaveRequest request) {
        String resultMessage = stateService.add(request);
        return ResponseEntity.ok(resultMessage);
    }

    @PutMapping("/update")
    public ResponseEntity<UsStateDto> updateState(@RequestBody UsStateUpdateRequest request) {
        UsStateDto updatedState = stateService.update(request);
        return ResponseEntity.ok(updatedState);
    }

    @GetMapping("{id}")
    public ResponseEntity<UsStateDto> getState(@PathVariable Short id) {
        UsStateDto state = stateService.findStateByStateId(id);
        return ResponseEntity.ok(state);
    }

    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteState(@PathVariable Short id) {
        stateService.deleteStateByStateId(id);
        return ResponseEntity.ok("İşlem Başarılı");
    }

    @GetMapping
    public ResponseEntity<List<UsStateDto>> findAllStates() {
        return ResponseEntity.ok(stateService.findAllStates());
    }
}
