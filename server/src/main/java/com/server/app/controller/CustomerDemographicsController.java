package com.server.app.controller;

import com.server.app.dto.request.customerDemographics.CustomerDemographicsSaveRequest;
import com.server.app.dto.request.customerDemographics.CustomerDemographicsUpdateRequest;
import com.server.app.dto.response.CustomerDemographicsDto;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.CustomerDemographicsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-demographics")
@RequiredArgsConstructor
public class CustomerDemographicsController {

    private final CustomerDemographicsService service;

    @PostMapping("/add")
    public ResponseEntity<GenericResponse> add(@RequestBody CustomerDemographicsSaveRequest request){
        return ResponseEntity.ok(service.add(request));
    }

    @PutMapping("/update")
    public ResponseEntity<GenericResponse> update(@RequestBody CustomerDemographicsUpdateRequest request){
        return ResponseEntity.ok(service.update(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataGenericResponse<CustomerDemographicsDto>> findCustomerDemographicsByCustomerTypeId(@PathVariable String id){
        return ResponseEntity.ok(service.findCustomerDemographicsByCustomerTypeId(id));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteCustomerDemographicsByCustomerTypeId(@PathVariable String id){
        return ResponseEntity.ok(service.deleteCustomerDemographicsByCustomerTypeId(id));
    }

    @GetMapping
    public ResponseEntity<DataGenericResponse<List<CustomerDemographicsDto>>> findAll(){
        return ResponseEntity.ok(service.findAllCustomerDemographics());
    }
}
