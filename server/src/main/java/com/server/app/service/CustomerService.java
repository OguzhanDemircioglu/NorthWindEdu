package com.server.app.service;

import com.server.app.dto.response.CustomerDto;
import com.server.app.dto.request.customer.CustomerSaveRequest;
import com.server.app.dto.request.customer.CustomerUpdateRequest;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.Customer;

import java.util.List;

public interface CustomerService {

    GenericResponse add(CustomerSaveRequest request);

    GenericResponse update(CustomerUpdateRequest request);

    DataGenericResponse<CustomerDto> findCustomerByCustomerId(String customerId);

    GenericResponse deleteCustomerByCustomerId(String customerId);

    DataGenericResponse<List<CustomerDto>> findAllCustomers();

    Customer getCustomer(String customerId);
}