package com.server.app.mapper;

import com.server.app.dto.request.customer.CustomerSaveRequest;
import com.server.app.dto.request.customer.CustomerUpdateRequest;
import com.server.app.dto.response.CustomerDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.model.Customer;
import com.server.app.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerMapper {
    private final CustomerRepository repository;

    public CustomerDto toDto(Customer request) {
        return CustomerDto.builder()
                .customerId(request.getCustomerId())
                .companyName(request.getCompanyName())
                .fax(request.getFax())
                .address(request.getAddress())
                .phone(request.getPhone())
                .city(request.getCity())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .region(request.getRegion())
                .contactTitle(request.getContactTitle())
                .contactName(request.getContactName())
                .build();
    }

    public Customer toEntity(CustomerUpdateRequest request) {
        if(request.getCustomerId() == null || request.getCustomerId().isEmpty()) {
            throw new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        boolean isExist = repository.existsCustomerByCustomerId(request.getCustomerId());
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        return updateEntityFromRequest(request);
    }

    private Customer updateEntityFromRequest(CustomerUpdateRequest request) {
        return Customer.builder()
                .customerId(request.getCustomerId())
                .companyName(request.getCompanyName())
                .fax(request.getFax())
                .address(request.getAddress())
                .phone(request.getPhone())
                .city(request.getCity())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .region(request.getRegion())
                .contactTitle(request.getContactTitle())
                .contactName(request.getContactName())
                .build();
    }

    public Customer saveEntityFromRequest(CustomerSaveRequest request) {
        return Customer.builder()
                .customerId(request.getCustomerId())
                .companyName(request.getCompanyName())
                .fax(request.getFax())
                .address(request.getAddress())
                .phone(request.getPhone())
                .city(request.getCity())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .region(request.getRegion())
                .contactTitle(request.getContactTitle())
                .contactName(request.getContactName())
                .build();
    }
}