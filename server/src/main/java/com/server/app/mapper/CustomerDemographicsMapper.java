package com.server.app.mapper;

import com.server.app.dto.request.customerDemographics.CustomerDemographicsSaveRequest;
import com.server.app.dto.request.customerDemographics.CustomerDemographicsUpdateRequest;
import com.server.app.dto.response.CustomerDemographicsDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.model.CustomerDemographics;
import com.server.app.repository.CustomerDemographicsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerDemographicsMapper {

    private final CustomerDemographicsRepository repository;

    public CustomerDemographicsDto toDto(CustomerDemographics request) {
        return CustomerDemographicsDto.builder()
                .customerTypeId(request.getCustomerTypeId())
                .customerDesc(request.getCustomerDesc())
                .build();
    }

    public CustomerDemographics toEntity(CustomerDemographicsUpdateRequest request) {
        if (request.getCustomerTypeId() == null || request.getCustomerTypeId().isEmpty()) {
            throw new BusinessException(ResultMessages.EMPTY_CUSTOMER_TYPE_ID);
        }

        boolean isExist = repository.existsCustomerDemographicsByCustomerTypeId(request.getCustomerTypeId());
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        return updateEntityFromRequest(request);
    }

    private CustomerDemographics updateEntityFromRequest(CustomerDemographicsUpdateRequest request) {
        return CustomerDemographics.builder()
                .customerTypeId(request.getCustomerTypeId())
                .customerDesc(request.getCustomerDesc())
                .build();
    }

    public CustomerDemographics saveEntityFromRequest(CustomerDemographicsSaveRequest request) {
        if (request.getCustomerTypeId() == null || request.getCustomerTypeId().isEmpty()) {
            throw new BusinessException(ResultMessages.EMPTY_CUSTOMER_TYPE_ID);
        }
        return CustomerDemographics.builder()
                .customerTypeId(request.getCustomerTypeId())
                .customerDesc(request.getCustomerDesc())
                .build();
    }
}
