package com.server.app.dto.request.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSaveRequest {

    private String customerId;
    private Long employeeId;
    private Long shipViaId;

    private LocalDate orderDate;
    private LocalDate requiredDate;
    private LocalDate shippedDate;

    private Double freight;
    private String shipName;
    private String shipAddress;
    private String shipCity;
    private String shipRegion;
    private String shipPostalCode;
    private String shipCountry;
}
