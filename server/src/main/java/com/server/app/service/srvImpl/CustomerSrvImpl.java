package com.server.app.service.srvImpl;

import com.server.app.dto.CustomerDto;
import com.server.app.dto.request.CustomerSaveRequest;
import com.server.app.dto.request.CustomerUpdateRequest;
import com.server.app.model.Customer;
import com.server.app.repository.CustomerRepository;
import com.server.app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerSrvImpl implements CustomerService {

    private final CustomerRepository repository;

    @Override
    public String add(CustomerSaveRequest request) {

        try {
            repository.save(
                    Customer.builder()
                            .customerId(request.getCustomerId())
                            .city(request.getCity())
                            .country(request.getCountry())
                            .fax(request.getFax())
                            .phone(request.getPhone())
                            .companyName(request.getCompanyName())
                            .address(request.getAddress())
                            .postalCode(request.getPostalCode())
                            .region(request.getRegion())
                            .contactName(request.getContactName())
                            .contactTitle(request.getContactTitle())
                            .build());
        } catch (Exception e) {
            e.printStackTrace();
            return "İşlem Başarısız";
        }
        return "İşlem Başarılı";
    }

    public CustomerDto update(CustomerUpdateRequest request) {
        CustomerDto result = new CustomerDto();
        Optional<Customer> customer = repository.findCustomerByCustomerId(request.getCustomerId());
        if (customer.isEmpty()) {
            throw new RuntimeException("Update Edilecek Kayıt bulunamadı");
        }

        customer.get().setCity(request.getCity());
        customer.get().setCountry(request.getCountry());
        customer.get().setFax(request.getFax());
        customer.get().setPhone(request.getPhone());
        customer.get().setCompanyName(request.getCompanyName());
        customer.get().setAddress(request.getAddress());
        customer.get().setPostalCode(request.getPostalCode());
        customer.get().setRegion(request.getRegion());
        customer.get().setContactName(request.getContactName());
        customer.get().setContactTitle(request.getContactTitle());

        result.setCustomerId(customer.get().getCustomerId());
        result.setCity(customer.get().getCity());
        result.setCountry(customer.get().getCountry());
        result.setFax(customer.get().getFax());
        result.setPhone(customer.get().getPhone());
        result.setCompanyName(customer.get().getCompanyName());
        result.setAddress(customer.get().getAddress());
        result.setPostalCode(customer.get().getPostalCode());
        result.setRegion(customer.get().getRegion());
        result.setContactName(customer.get().getContactName());
        result.setContactTitle(customer.get().getContactTitle());

        repository.save(customer.get());
        return result;
    }

    @Override
    public CustomerDto findCustomerByCustomerId(String customerId) {
        CustomerDto result = new CustomerDto();

        Optional<Customer> customer = repository.findCustomerByCustomerId(customerId);
        if (customer.isEmpty()) {
            throw new RuntimeException("Update Edilecek Kayıt bulunamadı");
        }

        result.setCustomerId(customer.get().getCustomerId());
        result.setCity(customer.get().getCity());
        result.setCountry(customer.get().getCountry());
        result.setFax(customer.get().getFax());
        result.setPhone(customer.get().getPhone());
        result.setCompanyName(customer.get().getCompanyName());
        result.setAddress(customer.get().getAddress());
        result.setPostalCode(customer.get().getPostalCode());
        result.setRegion(customer.get().getRegion());
        result.setContactName(customer.get().getContactName());
        result.setContactTitle(customer.get().getContactTitle());

        return result;
    }

    @Override
    public void deleteCustomerByCustomerId(String customerId) {
        repository.deleteCustomerByCustomerId(customerId);
    }

    @Override
    public List<CustomerDto> findAllCustomers() {
        List<Customer> list = repository.findAll();
        List<CustomerDto> result = new ArrayList<>();

        for (Customer customer : list) {
            CustomerDto newCustomer = new CustomerDto();
            newCustomer.setCustomerId(customer.getCustomerId());
            newCustomer.setCity(customer.getCity());
            newCustomer.setCountry(customer.getCountry());
            newCustomer.setFax(customer.getFax());
            newCustomer.setPhone(customer.getPhone());
            newCustomer.setCompanyName(customer.getCompanyName());
            newCustomer.setAddress(customer.getAddress());
            newCustomer.setPostalCode(customer.getPostalCode());
            newCustomer.setRegion(customer.getRegion());
            newCustomer.setContactName(customer.getContactName());
            newCustomer.setContactTitle(customer.getContactTitle());

            result.add(newCustomer);
        }

        return result;
    }
}
