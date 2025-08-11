package com.server.app.service.srvImpl;

import com.server.app.model.Customer;
import com.server.app.repository.CustomerRepository;
import com.server.app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerSrvImpl implements CustomerService {

    private final CustomerRepository repository;

    @Override
    public Customer findCustomerByCustomerId(String customerId) {
        return repository.findCustomerByCustomerId(customerId);
    }

    @Override
    public Optional<Customer> findCustomerByContactName(String contactName) {
        return repository.findCustomerByContactName(contactName);
    }

    @Override
    public List<Customer> findAllCustomers() {
        return repository.findAll();
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return repository.save(customer);
    }

    @Override
    public void deleteCustomerByCustomerId(String customerId) {
        repository.deleteCustomerByCustomerId(customerId);
    }
}
