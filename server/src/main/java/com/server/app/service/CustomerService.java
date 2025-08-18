package com.server.app.service;

import com.server.app.dto.CustomerDto;
import com.server.app.dto.request.CustomerSaveRequest;
import com.server.app.dto.request.CustomerUpdateRequest;
import com.server.app.model.Customer;

import java.util.List;

public interface CustomerService {

    String add(CustomerSaveRequest request);

    CustomerDto update(CustomerUpdateRequest request);

    CustomerDto findCustomerByCustomerId(String customerId);

    void deleteCustomerByCustomerId(String customerId);

    boolean existsByCustomerId(String customerId);

    List<CustomerDto> findAllCustomers();
}


