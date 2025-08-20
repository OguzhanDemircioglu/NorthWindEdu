package com.server.app.mapper;

import com.server.app.dto.request.CustomerCustomerDemo.CustomerCustomerDemoSaveRequest;
import com.server.app.dto.request.CustomerCustomerDemo.CustomerCustomerDemoUpdateRequest;
import com.server.app.dto.response.CustomerCustomerDemoDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.model.CcdId;
import com.server.app.model.Customer;
import com.server.app.model.CustomerCustomerDemo;
import com.server.app.model.CustomerDemographics;
import com.server.app.repository.CustomerCustomerDemoRepository;
import com.server.app.service.CustomerDemographicsService;
import com.server.app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomerCustomerDemoMapper {

    private final CustomerCustomerDemoRepository repository;
    private final CustomerService customerService;
    private final CustomerDemographicsService customerDemographicsService;

    public CustomerCustomerDemoDto toDto(CustomerCustomerDemo request) {
        return CustomerCustomerDemoDto.builder()
                .customerId(
                        Objects.isNull(request.getCustomer())
                        ? null
                                : request.getCustomer().getCustomerId()
                )
                .customerTypeId(
                        Objects.isNull(request.getCustomerDemographics())
                        ? null
                                :request.getCustomerDemographics().getCustomerTypeId()
                )
                .build();
    }

    public CustomerCustomerDemo toEntity(CustomerCustomerDemoUpdateRequest request) {
        if (request.getCustomerId() == null || request.getCustomerId().isEmpty() ||
                request.getCustomerTypeId() == null || request.getCustomerTypeId().isEmpty()) {
            throw new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        boolean isExist = repository.existsByCcdId_CustomerIdAndCcdId_CustomerTypeId(request.getCustomerId(), request.getCustomerTypeId());
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        Customer customer = customerService.getCustomer(request.getCustomerId());
        if(Objects.isNull(customer)) {
            throw new BusinessException(ResultMessages.CUSTOMER_NOT_FOUND);
        }

        CustomerDemographics customerDemographics = customerDemographicsService.getCustomerDemographics(request.getCustomerTypeId());
        if(Objects.isNull(customerDemographics)) {
            throw new BusinessException(ResultMessages.CUSTOMER_DEMOGRAPHICS_NOT_FOUND);
        }

        return updateEntityFromRequest(request, customer, customerDemographics);
    }

    private CustomerCustomerDemo updateEntityFromRequest(CustomerCustomerDemoUpdateRequest request, Customer customer, CustomerDemographics customerDemographics) {
        return CustomerCustomerDemo.builder()
                .ccdId(new CcdId(request.getCustomerId(), request.getCustomerTypeId()))
                .customer(customer)
                .customerDemographics(customerDemographics)
                .build();
    }

    public CustomerCustomerDemo saveEntityFromRequest(CustomerCustomerDemoSaveRequest request) {
        Customer customer = customerService.getCustomer(request.getCustomerId());
        if(Objects.isNull(customer)) {
            throw new BusinessException(ResultMessages.CUSTOMER_NOT_FOUND);
        }

        CustomerDemographics customerDemographics = customerDemographicsService.getCustomerDemographics(request.getCustomerTypeId());
        if(Objects.isNull(customerDemographics)) {
            throw new BusinessException(ResultMessages.CUSTOMER_DEMOGRAPHICS_NOT_FOUND);
        }

        return CustomerCustomerDemo.builder()
                .ccdId(new CcdId(request.getCustomerId(), request.getCustomerTypeId()))
                .customer(customer)
                .customerDemographics(customerDemographics)
                .build();
    }
}
