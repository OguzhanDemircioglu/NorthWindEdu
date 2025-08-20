package com.server.app.service;

import com.server.app.dto.request.customerCustomerDemo.CustomerCustomerDemoSaveRequest;
import com.server.app.dto.request.customerCustomerDemo.CustomerCustomerDemoUpdateRequest;
import com.server.app.dto.response.CustomerCustomerDemoDto;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.embedded.CustomerCustomerDemoId;

import java.util.List;

public interface CustomerCustomerDemoService {

    GenericResponse add(CustomerCustomerDemoSaveRequest request);

    GenericResponse update(CustomerCustomerDemoUpdateRequest request);

    DataGenericResponse<CustomerCustomerDemoDto> findCustomerCustomerDemoByCustomerCustomerDemoId(CustomerCustomerDemoId id);

    GenericResponse deleteCustomerCustomerDemoByCustomerCustomerDemoId(CustomerCustomerDemoId id);

    DataGenericResponse<List<CustomerCustomerDemoDto>> findAllCustomerCustomerDemos();
}
