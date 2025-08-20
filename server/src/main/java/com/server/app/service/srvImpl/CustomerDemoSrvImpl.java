package com.server.app.service.srvImpl;

import com.google.common.base.Strings;
import com.server.app.dto.request.customerDemo.CustomerDemoSaveRequest;
import com.server.app.dto.request.customerDemo.CustomerDemoUpdateRequest;
import com.server.app.dto.response.CustomerDemoDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.CustomerDemoMapper;
import com.server.app.model.embedded.CustomerDemoId;
import com.server.app.model.CustomerDemo;
import com.server.app.repository.CustomerDemoRepository;
import com.server.app.service.CustomerDemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerDemoSrvImpl implements CustomerDemoService {

    private final CustomerDemoRepository repository;
    private final CustomerDemoMapper mapper;

    @Override
    public GenericResponse add(CustomerDemoSaveRequest request) {
        CustomerDemo customerDemo = mapper.saveEntityFromRequest(request);
        BusinessRules.validate(
                checkCustomerDemoForGeneralValidations(customerDemo)
        );
        repository.save(customerDemo);
        return new GenericResponse();
    }

    @Override
    public GenericResponse update(CustomerDemoUpdateRequest request) {
        CustomerDemo customerDemo = mapper.toEntity(request);

        BusinessRules.validate(
                checkCustomerDemoForGeneralValidations(customerDemo)
        );

        repository.save(customerDemo);
        return GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).build();
    }

    @Override
    public DataGenericResponse<CustomerDemoDto> findCustomerDemoByCustomerDemoId(CustomerDemoId id) {
        Optional<CustomerDemo> customerCustomerDemo = repository.findCustomerCustomerDemoByCustomerDemoId(id);
        if (customerCustomerDemo.isEmpty()) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }
        CustomerDemoDto dto = mapper.toDto(customerCustomerDemo.get());

        return DataGenericResponse.<CustomerDemoDto>dataBuilder()
                .data(dto)
                .build();
    }

    @Override
    public GenericResponse deleteCustomerDemoByCustomerDemoId(CustomerDemoId id) {
        boolean isExists = repository.existsByCustomerDemoId_CustomerIdAndCustomerDemoId_CustomerTypeId(id.getCustomerId(), id.getCustomerTypeId());
        if (!isExists) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }
        repository.deleteCustomerDemoByCustomerDemoId(id);

        return GenericResponse.builder().message(ResultMessages.RECORD_DELETED).build();
    }

    @Override
    public DataGenericResponse<List<CustomerDemoDto>> findAllCustomerDemos() {
        List<CustomerDemo> customerDemos =  repository.findAll();
        List<CustomerDemoDto> dtos = customerDemos.stream()
                .map(mapper::toDto)
                .toList();

        return DataGenericResponse.<List<CustomerDemoDto>>dataBuilder()
                .data(dtos)
                .build();
    }

    private String checkCustomerDemoForGeneralValidations(CustomerDemo request) {
        if(Strings.isNullOrEmpty(request.getCustomerDemoId().getCustomerId())) {
            return ResultMessages.EMPTY_CUSTOMER_ID;
        }

        if(Strings.isNullOrEmpty(request.getCustomerDemoId().getCustomerTypeId())) {
            return ResultMessages.EMPTY_CUSTOMER_TYPE_ID;
        }
        return null;
    }
}
