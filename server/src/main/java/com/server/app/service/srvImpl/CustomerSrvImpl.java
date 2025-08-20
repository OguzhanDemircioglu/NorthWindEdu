package com.server.app.service.srvImpl;

import com.google.common.base.Strings;
import com.server.app.dto.response.CustomerDto;
import com.server.app.dto.request.customer.CustomerSaveRequest;
import com.server.app.dto.request.customer.CustomerUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.CustomerMapper;
import com.server.app.model.Customer;
import com.server.app.repository.CustomerRepository;
import com.server.app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerSrvImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Override
    public GenericResponse add(CustomerSaveRequest request) {
        Customer customer = mapper.saveEntityFromRequest(request);

        BusinessRules.validate(
                checkCustomerForGeneralValidations(customer),
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

        repository.save(customer);
        return new GenericResponse();
    }

    public GenericResponse update(CustomerUpdateRequest request) {
        Customer customer = mapper.toEntity(request);

        BusinessRules.validate(
                checkCustomerForGeneralValidations(customer),
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

        repository.save(customer);
        return GenericResponse.builder()
                .message(ResultMessages.RECORD_UPDATED)
                .build();
    }

    @Override
    public DataGenericResponse<CustomerDto> findCustomerByCustomerId(String customerId) {
        Optional<Customer> customer = repository.findCustomerByCustomerId(customerId);
        if (customer.isEmpty()) {
            throw new BusinessException(ResultMessages.CUSTOMER_NOT_FOUND);
        }

        CustomerDto dto = mapper.toDto(customer.get());

        return DataGenericResponse.<CustomerDto>dataBuilder()
                .data(dto)
                .build();
    }

    @Override
    public GenericResponse deleteCustomerByCustomerId(String customerId) {
        boolean isExist = repository.existsCustomerByCustomerId(customerId);
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        repository.deleteCustomerByCustomerId(customerId);

        return GenericResponse.builder().message(ResultMessages.RECORD_DELETED).build();
    }

    @Override
    public DataGenericResponse<List<CustomerDto>> findAllCustomers() {
        List<Customer> customers = repository.findAll();
        List<CustomerDto> dtos = customers.stream()
                .map(mapper::toDto)
                .toList();

        return DataGenericResponse.<List<CustomerDto>>dataBuilder()
                .data(dtos)
                .build();
    }

    @Override
    public Customer getCustomer(String customerId) {
        return repository.getCustomerByCustomerId(customerId);
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

    private String checkCityValidation(String city) {
        if (!Strings.isNullOrEmpty(city) && city.length() > 30) {
            return ResultMessages.CITY_OUT_OF_RANGE;
        }

        if (!Strings.isNullOrEmpty(city) && !city.matches("^[\\p{L} ]+$")) {
            return ResultMessages.WRONG_CITY_FORMAT;
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

    private String checkPhoneFormat(String phone) {
        if(phone != null && phone.length() > 24) {
            return ResultMessages.PHONE_OUT_OF_RANGE;
        }

        if(phone != null && !phone.matches("^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$")) {
            return ResultMessages.WRONG_PHONE_FORMAT;
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

    private String checkRegionValidation(String region) {
        if (region != null && region.length() > 15) {
            return ResultMessages.REGION_OUT_OF_RANGE;
        }
        return null;
    }

    private String checkTitleValidation(String title) {
        if (title != null && title.length() > 30) {
            return ResultMessages.C_TITLE_OUT_OF_RANGE;
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

    private String checkCompanyValidation(String name) {
        if (!Strings.isNullOrEmpty(name) && name.length() > 40) {
            return ResultMessages.COMPANY_NAME_OUT_OF_RANGE;
        }
        return null;
    }



    private String checkCustomerForGeneralValidations(Customer request) {
        if(Strings.isNullOrEmpty(request.getCustomerId())) {
            return ResultMessages.ID_IS_NOT_DELIVERED;
        }

        if (Strings.isNullOrEmpty(request.getCompanyName())) {
            return ResultMessages.EMPTY_NAME;
        }
        return null;
    }
}
