package com.server.app.controller;

import com.server.app.dto.request.customerDemo.CustomerDemoSaveRequest;
import com.server.app.dto.request.customerDemo.CustomerDemoUpdateRequest;
import com.server.app.dto.response.CustomerDemoDto;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.embedded.CustomerDemoId;
import com.server.app.service.CustomerDemoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-demos")
@RequiredArgsConstructor
public class CustomerDemoController {

    private final CustomerDemoService service;

    @PostMapping("/add")
    public ResponseEntity<GenericResponse> add(@RequestBody CustomerDemoSaveRequest request) {
        return ResponseEntity.ok(service.add(request));
    }

    @PutMapping("/update")
    public ResponseEntity<GenericResponse> update(@RequestBody CustomerDemoUpdateRequest request) {
        return ResponseEntity.ok(service.update(request));
    }

    @GetMapping("/")
    public ResponseEntity<DataGenericResponse<CustomerDemoDto>> get(
            @RequestParam String customerId,
            @RequestParam String customerTypeId) {
        CustomerDemoId id = new CustomerDemoId(customerId, customerTypeId);

        DataGenericResponse<CustomerDemoDto> result = service.findCustomerDemoByCustomerDemoId(id);
        return ResponseEntity.ok(result);
    }

    @Transactional
    @DeleteMapping("/")
    public ResponseEntity<GenericResponse> delete(
            @RequestParam String customerId,
            @RequestParam String customerTypeId) {
        CustomerDemoId id = new CustomerDemoId(customerId, customerTypeId);

        GenericResponse result = service.deleteCustomerDemoByCustomerDemoId(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<DataGenericResponse<List<CustomerDemoDto>>> findAll() {
        DataGenericResponse<List<CustomerDemoDto>> result = service.findAllCustomerDemos();
        return ResponseEntity.ok(result);
    }

}
