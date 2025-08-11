package com.server.app.service;

import com.server.app.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Customer findCustomerByCustomerId(String customerId);

    Optional<Customer> findCustomerByContactName(String contactName);

    List<Customer> findAllCustomers();

    Customer saveCustomer(Customer customer);

    void deleteCustomerByCustomerId(String customerId);
}
