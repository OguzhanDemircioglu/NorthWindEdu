package com.server.app.service.srvImpl;

import com.google.common.base.Strings;
import com.server.app.dto.ShipperDto;
import com.server.app.dto.request.ShipperSaveRequest;
import com.server.app.dto.request.ShipperUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.ShipperMapper;
import com.server.app.model.Shipper;
import com.server.app.repository.ShipperRepository;
import com.server.app.service.ShipperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShipperSrvImpl implements ShipperService {

    private final ShipperRepository repository;
    private final ShipperMapper mapper;

    @Override
    public GenericResponse add(ShipperSaveRequest request) {
        Shipper shipper = mapper.saveEntityFromRequest(request);

        BusinessRules.validate(
                checkShipperForGeneralValidations(shipper),
                checkCompanyNameValidation(shipper.getCompanyName())
        );

        repository.save(shipper);
        return new GenericResponse();
    }

    @Override
    public GenericResponse update(ShipperUpdateRequest request) {
        Shipper shipper = mapper.toEntity(request);

        BusinessRules.validate(
                checkShipperForGeneralValidations(shipper),
                checkCompanyNameValidation(shipper.getCompanyName())
        );

        repository.save(shipper);
        return GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).build();
    }

    @Override
    public DataGenericResponse<ShipperDto> findShipperByShipperId(Long id) {
        Optional<Shipper> shipper = repository.findShipperByShipperId(id);
        if (shipper.isEmpty()) {
            throw new BusinessException(ResultMessages.SHIPPER_NOT_FOUND);
        }

        ShipperDto dto = mapper.toDto(shipper.get());
        return DataGenericResponse.<ShipperDto>dataBuilder()
                .data(dto)
                .build();
    }

    @Override
    public GenericResponse deleteShipperByShipperId(Long id) {
        boolean isExist = repository.existsShipperByShipperId(id);
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        repository.deleteShipperByShipperId(id);
        return GenericResponse.builder().message(ResultMessages.RECORD_DELETED).build();
    }

    @Override
    public DataGenericResponse<List<ShipperDto>> findAllShippers() {
        List<ShipperDto> dtos = repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();

        return DataGenericResponse.<List<ShipperDto>>dataBuilder()
                .data(dtos)
                .build();
    }

    @Override
    public Shipper getShipper(Long shipperId) {

        return repository.getShipperByShipperId(shipperId);
    }


    private String checkCompanyNameValidation(String companyName) {
        if (!Strings.isNullOrEmpty(companyName) && companyName.length() > 40) {
            return ResultMessages.COMPANY_NAME_OUT_OF_RANGE;
        }
        return null;
    }

    private String checkShipperForGeneralValidations(Shipper request) {
        if (Strings.isNullOrEmpty(request.getCompanyName())) {
            return ResultMessages.EMPTY_NAME;
        }
        return null;
    }
}
