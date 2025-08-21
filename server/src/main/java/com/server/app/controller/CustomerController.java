package com.server.app.controller;

import com.server.app.dto.response.CustomerDto;
import com.server.app.dto.request.customer.CustomerSaveRequest;
import com.server.app.dto.request.customer.CustomerUpdateRequest;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/add")
    public ResponseEntity<GenericResponse> add(@RequestBody CustomerSaveRequest request){
        return ResponseEntity.ok(customerService.add(request));
    }

    @PutMapping("/update")
    public ResponseEntity<GenericResponse> updateCustomer(@RequestBody CustomerUpdateRequest request){
        return ResponseEntity.ok(customerService.update(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse> findCustomerByCustomerId(@PathVariable String id){
        DataGenericResponse<CustomerDto> result = customerService.findCustomerByCustomerId(id);
        return ResponseEntity.ok(result);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> delete(@PathVariable String id){
        GenericResponse result = customerService.deleteCustomerByCustomerId(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<DataGenericResponse<List<CustomerDto>>> findAllCustomers(){
        DataGenericResponse<List<CustomerDto>> result = customerService.findAllCustomers();
        return ResponseEntity.ok(result);
    }
}
