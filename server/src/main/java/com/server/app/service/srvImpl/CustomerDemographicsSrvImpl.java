package com.server.app.service.srvImpl;

import com.google.common.base.Strings;
import com.server.app.dto.request.customerDemographics.CustomerDemographicsSaveRequest;
import com.server.app.dto.request.customerDemographics.CustomerDemographicsUpdateRequest;
import com.server.app.dto.response.CustomerDemographicsDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.CustomerDemographicsMapper;
import com.server.app.model.CustomerDemographics;
import com.server.app.repository.CustomerDemographicsRepository;
import com.server.app.service.CustomerDemographicsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerDemographicsSrvImpl implements CustomerDemographicsService {

    private final CustomerDemographicsRepository repository;
    private final CustomerDemographicsMapper mapper;

    @Override
    public GenericResponse add(CustomerDemographicsSaveRequest request) {
        CustomerDemographics entity = mapper.saveEntityFromRequest(request);

        BusinessRules.validate(
                checkGeneralValidations(entity)
        );

        repository.save(entity);
        return new GenericResponse();
    }

    @Override
    public GenericResponse update(CustomerDemographicsUpdateRequest request) {
        CustomerDemographics entity = mapper.toEntity(request);

        BusinessRules.validate(
                checkGeneralValidations(entity)
        );

        repository.save(entity);
        return GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).build();
    }

    @Override
    public DataGenericResponse<CustomerDemographicsDto> findCustomerDemographicsByCustomerTypeId(String customerTypeId) {
        Optional<CustomerDemographics> opt = repository.findCustomerDemographicsByCustomerTypeId(customerTypeId);
        if (opt.isEmpty()) {
            throw new BusinessException(ResultMessages.CUSTOMER_DEMOGRAPHICS_NOT_FOUND);
        }
        CustomerDemographicsDto dto = mapper.toDto(opt.get());
        return DataGenericResponse.<CustomerDemographicsDto>dataBuilder()
                .data(dto)
                .build();
    }

    @Override
    public GenericResponse deleteCustomerDemographicsByCustomerTypeId(String customerTypeId) {
        boolean isExist = repository.existsCustomerDemographicsByCustomerTypeId(customerTypeId);
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }
        repository.deleteCustomerDemographicsByCustomerTypeId(customerTypeId);
        return GenericResponse.builder().message(ResultMessages.RECORD_DELETED).build();
    }

    @Override
    public DataGenericResponse<List<CustomerDemographicsDto>> findAllCustomerDemographics() {
        List<CustomerDemographicsDto> dtos = repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();

        return DataGenericResponse.<List<CustomerDemographicsDto>>dataBuilder()
                .data(dtos)
                .build();
    }

    @Override
    public CustomerDemographics getCustomerDemographics(String customerTypeId) {
        return repository.getCustomerDemographicsByCustomerTypeId(customerTypeId);
    }

    private String checkGeneralValidations(CustomerDemographics request) {
        if (request == null || Strings.isNullOrEmpty(request.getCustomerTypeId())) {
            return ResultMessages.EMPTY_CUSTOMER_TYPE_ID;
        }
        if (request.getCustomerTypeId().length() != 1) { return ResultMessages.CUSTOMER_TYPE_ID_OUT_OF_RANGE; }
        return null;
    }
}
