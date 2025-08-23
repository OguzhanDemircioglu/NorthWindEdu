package com.server.app.mapper;

import com.server.app.dto.request.customerDemo.CustomerDemoSaveRequest;
import com.server.app.dto.request.customerDemo.CustomerDemoUpdateRequest;
import com.server.app.dto.response.CustomerDemoDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.model.embedded.CustomerDemoId;
import com.server.app.model.Customer;
import com.server.app.model.CustomerDemo;
import com.server.app.model.CustomerDemographics;
import com.server.app.repository.CustomerDemoRepository;
import com.server.app.service.CustomerDemographicsService;
import com.server.app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomerDemoMapper {

    private final CustomerDemoRepository repository;
    private final CustomerService customerService;
    private final CustomerDemographicsService customerDemographicsService;

    public CustomerDemoDto toDto(CustomerDemo request) {
        return CustomerDemoDto.builder()
                .customerId(
                        Objects.isNull(request.getCustomer())
                        ? null
                                : request.getCustomer().getCustomerId()
                )
                .customerTypeId(
                        Objects.isNull(request.getCustomerDemographics())
                        ? null
                                : request.getCustomerDemographics().getCustomerTypeId()
                )
                .build();
    }

    public CustomerDemo toEntity(CustomerDemoUpdateRequest request) {
        if (request.getCustomerId() == null || request.getCustomerId().isEmpty() ||
                request.getCustomerTypeId() == null || request.getCustomerTypeId().isEmpty()) {
            throw new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        boolean isExist = repository.existsByCustomerDemoId_CustomerIdAndCustomerDemoId_CustomerTypeId(request.getCustomerId(), request.getCustomerTypeId());
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        Customer customer = customerService.getCustomer(request.getCustomerId());


        CustomerDemographics customerDemographics = customerDemographicsService.getCustomerDemographics(request.getCustomerTypeId());
        if(Objects.isNull(customerDemographics)) {
            throw new BusinessException(ResultMessages.CUSTOMER_DEMOGRAPHICS_NOT_FOUND);
        }

        return updateEntityFromRequest(request, customer, customerDemographics);
    }

    private CustomerDemo updateEntityFromRequest(CustomerDemoUpdateRequest request, Customer customer, CustomerDemographics customerDemographics) {
        return CustomerDemo.builder()
                .customerDemoId(new CustomerDemoId(request.getCustomerId(), request.getCustomerTypeId()))
                .customer(customer)
                .customerDemographics(customerDemographics)
                .build();
    }

    public CustomerDemo saveEntityFromRequest(CustomerDemoSaveRequest request) {
        Customer customer = customerService.getCustomer(request.getCustomerId());

        CustomerDemographics customerDemographics = customerDemographicsService.getCustomerDemographics(request.getCustomerTypeId());
        if(Objects.isNull(customerDemographics)) {
            throw new BusinessException(ResultMessages.CUSTOMER_DEMOGRAPHICS_NOT_FOUND);
        }

        return CustomerDemo.builder()
                .customerDemoId(new CustomerDemoId(request.getCustomerId(), request.getCustomerTypeId()))
                .customer(customer)
                .customerDemographics(customerDemographics)
                .build();
    }
}
