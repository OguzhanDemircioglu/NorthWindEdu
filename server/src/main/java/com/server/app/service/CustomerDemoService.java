package com.server.app.service;

import com.server.app.dto.request.customerDemo.CustomerDemoSaveRequest;
import com.server.app.dto.request.customerDemo.CustomerDemoUpdateRequest;
import com.server.app.dto.response.CustomerDemoDto;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.embedded.CustomerDemoId;

import java.util.List;

public interface CustomerDemoService {

    GenericResponse add(CustomerDemoSaveRequest request);

    GenericResponse update(CustomerDemoUpdateRequest request);

    DataGenericResponse<CustomerDemoDto> findCustomerDemoByCustomerDemoId(CustomerDemoId id);

    GenericResponse deleteCustomerDemoByCustomerDemoId(CustomerDemoId id);

    DataGenericResponse<List<CustomerDemoDto>> findAllCustomerDemos();
}
