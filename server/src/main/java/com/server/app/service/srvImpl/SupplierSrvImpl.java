package com.server.app.service.srvImpl;

import com.google.common.base.Strings;
import com.server.app.dto.response.SupplierDto;
import com.server.app.dto.request.supplier.SupplierSaveRequest;
import com.server.app.dto.request.supplier.SupplierUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.SupplierMapper;
import com.server.app.model.Supplier;
import com.server.app.repository.SupplierRepository;
import com.server.app.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierSrvImpl implements SupplierService {

    private final SupplierRepository repository;
    private final SupplierMapper mapper;

    @Override
    public GenericResponse add(SupplierSaveRequest request) {
        Supplier supplier = mapper.saveEntityFromRequest(request);

        BusinessRules.validate(
                checkSupplierForGeneralValidations(supplier),
                checkAddressValidation(request.getAddress()),
                checkCityValidation(request.getCity()),
                checkCountryValidation(request.getCountry()),
                checkPhoneFormat(request.getPhone()),
                checkFaxFormat(request.getFax()),
                checkNameValidation(request.getContactName()),
                checkCompanyValidation(request.getCompanyName()),
                checkRegionValidation(request.getRegion()),
                checkPostalCodeValidation(request.getPostalCode()),
                checkTitleValidation(request.getContactTitle())
        );

        repository.save(supplier);
        return new GenericResponse();
    }

    @Override
    public GenericResponse update(SupplierUpdateRequest request) {
        Supplier supplier = mapper.toEntity(request);

        BusinessRules.validate(
                checkSupplierForGeneralValidations(supplier),
                checkAddressValidation(request.getAddress()),
                checkCityValidation(request.getCity()),
                checkCountryValidation(request.getCountry()),
                checkPhoneFormat(request.getPhone()),
                checkFaxFormat(request.getFax()),
                checkNameValidation(request.getContactName()),
                checkCompanyValidation(request.getCompanyName()),
                checkRegionValidation(request.getRegion()),
                checkPostalCodeValidation(request.getPostalCode()),
                checkTitleValidation(request.getContactTitle())
        );

        repository.save(supplier);
        return GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).build();
    }

    @Override
    public DataGenericResponse<SupplierDto> findSupplierBySupplierId(Long supplierId) {
        Optional<Supplier> supplier = repository.findSupplierBySupplierId(supplierId);
        if ( supplier.isEmpty()) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        SupplierDto dto = mapper.toDto(supplier.get());
        return DataGenericResponse.<SupplierDto>dataBuilder()
                .data(dto)
                .build();
    }

    @Override
    public GenericResponse deleteSupplierBySupplierId(Long supplierId) {
        boolean isExist = repository.existsSupplierBySupplierId(supplierId);
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }
        repository.deleteSupplierBySupplierId(supplierId);

        if (repository.count() == 0) {
            repository.resetSupplierSequence();
        }

        return GenericResponse.builder().message(ResultMessages.RECORD_DELETED).build();
    }

    @Override
    public DataGenericResponse<List<SupplierDto>> findAllSuppliers() {
        List<Supplier> suppliers = repository.findAll();
        List<SupplierDto> dtos = suppliers.stream()
                .map(mapper::toDto)
                .toList();

        return DataGenericResponse.<List<SupplierDto>>dataBuilder()
                .data(dtos)
                .build();
    }

    @Override
    public Supplier getSupplier(Long supplierId) {
        return repository.getSupplierBySupplierId(supplierId);
    }

    private String checkSupplierForGeneralValidations(Supplier request) {
        if (Strings.isNullOrEmpty(request.getCompanyName())) {
            return ResultMessages.EMPTY_COMPANY_NAME;
        }

        return null;
    }

    private String checkCompanyValidation(String name) {
        if (!Strings.isNullOrEmpty(name) && name.length() > 40) {
            return ResultMessages.COMPANY_NAME_OUT_OF_RANGE;
        }
        return null;
    }

    private String checkPostalCodeValidation(String postalCode) {
        if(postalCode != null && postalCode.length() > 10) {
            return ResultMessages.POSTAL_CODE_OUT_OF_RANGE;
        }

        if (postalCode != null && !postalCode.matches("^[0-9]+$")) {
            return ResultMessages.WRONG_POSTAL_CODE_FORMAT;
        }
        return null;
    }

    private String checkTitleValidation(String title) {
        if (title != null && title.length() > 30) {
            return ResultMessages.C_TITLE_OUT_OF_RANGE;
        }
        return null;
    }

    private String checkRegionValidation(String region) {
        if (region != null && region.length() > 15) {
            return ResultMessages.REGION_OUT_OF_RANGE;
        }
        return null;
    }

    private String checkFaxFormat(String fax) {
        if(fax != null && fax.length() > 24) {
            return ResultMessages.FAX_OUT_OF_RANGE;
        }

        if(fax != null && !fax.matches("^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$")) {
            return ResultMessages.WRONG_FAX_FORMAT;
        }
        return null;
    }

    private String checkPhoneFormat(String phone) {
        if(phone != null && phone.length() > 24) {
            return ResultMessages.PHONE_OUT_OF_RANGE;
        }

        if(phone != null && !phone.matches("^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$")) {
            return ResultMessages.WRONG_PHONE_FORMAT;
        }
        return null;
    }

    private String checkCountryValidation(String country) {
        if (!Strings.isNullOrEmpty(country) && country.length() > 15) {
            return ResultMessages.COUNTRY_OUT_OF_RANGE;
        }

        if (!Strings.isNullOrEmpty(country) && !country.matches("^[\\p{L} ]+$")) {
            return ResultMessages.WRONG_COUNTRY_FORMAT;
        }
        return null;
    }

    private String checkCityValidation(String city) {
        if (!Strings.isNullOrEmpty(city) && city.length() > 30) {
            return ResultMessages.CITY_OUT_OF_RANGE;
        }

        if (!Strings.isNullOrEmpty(city) && !city.matches("^[\\p{L} ]+$")) {
            return ResultMessages.WRONG_CITY_FORMAT;
        }
        return null;
    }

    private String checkNameValidation(String name) {
        if (!Strings.isNullOrEmpty(name) && name.length() > 30) {
            return ResultMessages.CONTACT_NAME_OUT_OF_RANGE;
        }
        return null;
    }

    private String checkAddressValidation(String address) {
        if (!Strings.isNullOrEmpty(address) && address.length() > 30) {
            return ResultMessages.ADDRESS_OUT_OF_RANGE;
        }
        return null;
    }

}
