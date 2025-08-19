package com.server.app.dto.request.CustomerCustomerDemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCustomerDemoUpdateRequest {
    private String customerId;
    private String customerTypeId;
}
