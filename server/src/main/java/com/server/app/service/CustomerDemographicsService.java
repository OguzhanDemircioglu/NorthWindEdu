package com.server.app.service;

import com.server.app.dto.request.customerDemographics.CustomerDemographicsSaveRequest;
import com.server.app.dto.request.customerDemographics.CustomerDemographicsUpdateRequest;
import com.server.app.dto.response.CustomerDemographicsDto;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.CustomerDemographics;

import java.util.List;

public interface CustomerDemographicsService {

    GenericResponse add(CustomerDemographicsSaveRequest request);

    GenericResponse update(CustomerDemographicsUpdateRequest request);

    DataGenericResponse<CustomerDemographicsDto> findCustomerDemographicsByCustomerTypeId(String customerTypeId);

    GenericResponse deleteCustomerDemographicsByCustomerTypeId(String customerTypeId);

    DataGenericResponse<List<CustomerDemographicsDto>> findAllCustomerDemographics();

    CustomerDemographics getCustomerDemographics(String customerTypeId);
}
