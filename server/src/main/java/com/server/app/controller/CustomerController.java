package com.server.app.controller;

import com.server.app.dto.CustomerDto;
import com.server.app.dto.request.CustomerSaveRequest;
import com.server.app.dto.request.CustomerUpdateRequest;
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
    public ResponseEntity<?> add(@RequestBody CustomerSaveRequest request){
        String resultMessage = customerService.add(request);
        return ResponseEntity.ok(resultMessage);
    }

    @PutMapping("/update")
    public ResponseEntity<CustomerDto> updateCustomer(@RequestBody CustomerUpdateRequest request){
        CustomerDto updatedCustomer = customerService.update(request);
        return ResponseEntity.ok(updatedCustomer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> findCustomerByCustomerId(@PathVariable String id){
        CustomerDto customer = customerService.findCustomerByCustomerId(id);
        return ResponseEntity.ok(customer);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        customerService.deleteCustomerByCustomerId(id);
        return ResponseEntity.ok().body("İşlem Başarılı");
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> findAllCustomers(){
        return ResponseEntity.ok(customerService.findAllCustomers());
    }
}
