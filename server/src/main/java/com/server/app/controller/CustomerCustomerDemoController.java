package com.server.app.controller;

import com.server.app.dto.request.customerCustomerDemo.CustomerCustomerDemoSaveRequest;
import com.server.app.dto.request.customerCustomerDemo.CustomerCustomerDemoUpdateRequest;
import com.server.app.dto.response.CustomerCustomerDemoDto;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.embedded.CustomerCustomerDemoId;
import com.server.app.service.CustomerCustomerDemoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-customer-demo")
@RequiredArgsConstructor
public class CustomerCustomerDemoController {

    private final CustomerCustomerDemoService service;

    @PostMapping("/add")
    public ResponseEntity<GenericResponse> add(@RequestBody CustomerCustomerDemoSaveRequest request) {
        return ResponseEntity.ok(service.add(request));
    }

    @PutMapping("/update")
    public ResponseEntity<GenericResponse> update(@RequestBody CustomerCustomerDemoUpdateRequest request) {
        return ResponseEntity.ok(service.update(request));
    }

    @GetMapping("/")
    public ResponseEntity<GenericResponse> get(
            @RequestParam String customerId,
            @RequestParam String customerTypeId) {
        CustomerCustomerDemoId id = new CustomerCustomerDemoId(customerId, customerTypeId);

        DataGenericResponse<CustomerCustomerDemoDto> result = service.findCustomerCustomerDemoByCustomerCustomerDemoId(id);
        return ResponseEntity.ok(result);
    }

    @Transactional
    @DeleteMapping("/")
    public ResponseEntity<GenericResponse> delete(
            @RequestParam String customerId,
            @RequestParam String customerTypeId) {
        CustomerCustomerDemoId id = new CustomerCustomerDemoId(customerId, customerTypeId);

        GenericResponse result = service.deleteCustomerCustomerDemoByCustomerCustomerDemoId(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<DataGenericResponse<List<CustomerCustomerDemoDto>>> findAll() {
        DataGenericResponse<List<CustomerCustomerDemoDto>> result = service.findAllCustomerCustomerDemos();
        return ResponseEntity.ok(result);
    }

}
