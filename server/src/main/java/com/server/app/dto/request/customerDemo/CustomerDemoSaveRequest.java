package com.server.app.dto.request.customerDemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDemoSaveRequest {
    private String customerId;
    private String customerTypeId;
}
