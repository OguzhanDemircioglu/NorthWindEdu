package com.server.app.repository;

import com.server.app.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findCustomerByCustomerId(String customerId);

    void deleteCustomerByCustomerId(String customerId);

    Customer getCustomerByCustomerId(String customerId);
}