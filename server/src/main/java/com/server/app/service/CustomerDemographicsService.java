package com.server.app.service;

import com.server.app.dto.CustomerDemographicsDto;
import com.server.app.dto.request.CustomerDemographicsSaveRequest;
import com.server.app.dto.request.CustomerDemographicsUpdateRequest;
import com.server.app.model.CustomerDemographics;

import java.util.List;

public interface CustomerDemographicsService {

    String add(CustomerDemographicsSaveRequest request);

    CustomerDemographicsDto update(CustomerDemographicsUpdateRequest request);

    CustomerDemographicsDto findCustomerDemographicsByCustomerTypeId(String customerTypeId);

    void deleteCustomerDemographicsByCustomerTypeId(String customerTypeId);

    List<CustomerDemographicsDto> findAllCustomerDemographics();

    CustomerDemographics getCustomerDemographics(String customerTypeId);
}
