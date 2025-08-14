package com.server.app.controller;

import com.server.app.dto.CustomerDemographicsDto;
import com.server.app.dto.request.CustomerDemographicsSaveRequest;
import com.server.app.dto.request.CustomerDemographicsUpdateRequest;
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

    private final CustomerDemographicsService customerDemographicsService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody CustomerDemographicsSaveRequest request){
        String resultMessage = customerDemographicsService.add(request);
        return ResponseEntity.ok(resultMessage);
    }

    @PutMapping("/update")
    public ResponseEntity<CustomerDemographicsDto> update(@RequestBody CustomerDemographicsUpdateRequest request){
        CustomerDemographicsDto updated = customerDemographicsService.update(request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDemographicsDto> findById(@PathVariable String id){
        CustomerDemographicsDto dto = customerDemographicsService.findCustomerDemographicsByCustomerTypeId(id);
        return ResponseEntity.ok(dto);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        customerDemographicsService.deleteCustomerDemographicsByCustomerTypeId(id);
        return ResponseEntity.ok("İşlem Başarılı");
    }

    @GetMapping
    public ResponseEntity<List<CustomerDemographicsDto>> findAll(){
        return ResponseEntity.ok(customerDemographicsService.findAllCustomerDemographics());
    }
}
