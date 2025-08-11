package com.server.app.controller;

import com.server.app.model.Customer;
import com.server.app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor

public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer){
        Customer newCustomer = customerService.saveCustomer(customer);
        return ResponseEntity.ok(newCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String id, @RequestBody Customer customer){
        customer.setCustomerId(id);
        Customer updatedCustomer = customerService.saveCustomer(customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable String id){
        customerService.deleteCustomerByCustomerId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id){
        Customer customer = customerService.findCustomerByCustomerId(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/search")
    public ResponseEntity<Customer> getCustomerByName(@RequestParam String name){
        Optional<Customer> customer = customerService.findCustomerByContactName(name);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers(){
        List<Customer> customers = customerService.findAllCustomers();
        return ResponseEntity.ok(customers);
    }
}
