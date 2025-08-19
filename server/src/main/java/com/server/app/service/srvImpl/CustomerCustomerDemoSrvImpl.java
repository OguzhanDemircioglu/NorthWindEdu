package com.server.app.service.srvImpl;

import com.google.common.base.Strings;
import com.server.app.dto.request.CustomerCustomerDemo.CustomerCustomerDemoSaveRequest;
import com.server.app.dto.request.CustomerCustomerDemo.CustomerCustomerDemoUpdateRequest;
import com.server.app.dto.response.CustomerCustomerDemoDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.CustomerCustomerDemoMapper;
import com.server.app.model.CcdId;
import com.server.app.model.CustomerCustomerDemo;
import com.server.app.repository.CustomerCustomerDemoRepository;
import com.server.app.service.CustomerCustomerDemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerCustomerDemoSrvImpl implements CustomerCustomerDemoService {

    private final CustomerCustomerDemoRepository repository;
    private final CustomerCustomerDemoMapper mapper;

    @Override
    public GenericResponse add(CustomerCustomerDemoSaveRequest request) {
        CustomerCustomerDemo customerCustomerDemo = mapper.saveEntityFromRequest(request);
        BusinessRules.validate(
                checkCustomerCustomerDemoForGeneralValidations(customerCustomerDemo)
        );
        repository.save(customerCustomerDemo);
        return new GenericResponse();
    }

    @Override
    public GenericResponse update(CustomerCustomerDemoUpdateRequest request) {
        CustomerCustomerDemo customerCustomerDemo = mapper.toEntity(request);

        BusinessRules.validate(
                checkCustomerCustomerDemoForGeneralValidations(customerCustomerDemo)
        );

        repository.save(customerCustomerDemo);
        return GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).build();
    }

    @Override
    public DataGenericResponse<CustomerCustomerDemoDto> findCustomerCustomerDemoByCcdId(CcdId id) {
        Optional<CustomerCustomerDemo> customerCustomerDemo = repository.findCustomerCustomerDemoByCcdId(id);
        if (customerCustomerDemo.isEmpty()) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }
        CustomerCustomerDemoDto dto = mapper.toDto(customerCustomerDemo.get());

        return DataGenericResponse.<CustomerCustomerDemoDto>dataBuilder()
                .data(dto)
                .build();
    }

    @Override
    public GenericResponse deleteCustomerCustomerDemoByCcdId(CcdId id) {
        boolean isExists = repository.existsByCcdId_CustomerIdAndCcdId_CustomerTypeId(id.getCustomerId(), id.getCustomerTypeId());
        if (!isExists) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }
        repository.deleteCustomerCustomerDemoByCcdId(id);

        return GenericResponse.builder().message(ResultMessages.RECORD_DELETED).build();
    }

    @Override
    public DataGenericResponse<List<CustomerCustomerDemoDto>> findAllCustomerCustomerDemos() {
        List<CustomerCustomerDemo> customerCustomerDemos =  repository.findAll();
        List<CustomerCustomerDemoDto> dtos = customerCustomerDemos.stream()
                .map(mapper::toDto)
                .toList();

        return DataGenericResponse.<List<CustomerCustomerDemoDto>>dataBuilder()
                .data(dtos)
                .build();
    }

    private String checkCustomerCustomerDemoForGeneralValidations(CustomerCustomerDemo request) {
        if(Strings.isNullOrEmpty(request.getCcdId().getCustomerId())) {
            return ResultMessages.EMPTY_CUSTOMER_ID;
        }

        if(Strings.isNullOrEmpty(request.getCcdId().getCustomerTypeId())) {
            return ResultMessages.EMPTY_CUSTOMER_TYPE_ID;
        }
        return null;
    }
}
