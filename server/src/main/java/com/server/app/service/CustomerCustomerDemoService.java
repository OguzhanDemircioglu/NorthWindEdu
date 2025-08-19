package com.server.app.service;

import com.server.app.dto.request.CustomerCustomerDemo.CustomerCustomerDemoSaveRequest;
import com.server.app.dto.request.CustomerCustomerDemo.CustomerCustomerDemoUpdateRequest;
import com.server.app.dto.response.CustomerCustomerDemoDto;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.CcdId;

import java.util.List;

public interface CustomerCustomerDemoService {

    GenericResponse add(CustomerCustomerDemoSaveRequest request);

    GenericResponse update(CustomerCustomerDemoUpdateRequest request);

    DataGenericResponse<CustomerCustomerDemoDto> findCustomerCustomerDemoByCcdId(CcdId id);

    GenericResponse deleteCustomerCustomerDemoByCcdId(CcdId id);

    DataGenericResponse<List<CustomerCustomerDemoDto>> findAllCustomerCustomerDemos();
}
