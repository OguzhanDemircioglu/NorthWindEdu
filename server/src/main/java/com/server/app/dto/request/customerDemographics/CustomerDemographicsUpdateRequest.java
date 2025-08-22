package com.server.app.dto.request.customerDemographics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDemographicsUpdateRequest {
    private String customerTypeId;
    private String customerDesc;
}
