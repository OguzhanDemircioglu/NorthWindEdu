package com.server.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDto {
    private Short supplierId;
    private String companyName;
    private String contactName;
    private String contactTitle;
    private String address;
    private String region;
    private String postalCode;
    private String country;
    private String city;
    private String phone;
    private String fax;
    private String homepage;
}
