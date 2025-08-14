// File: src/main/java/com/server/app/dto/request/ShipperUpdateRequest.java
package com.server.app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipperUpdateRequest {
    private Short shipperId;
    private String companyName;
    private String phone;
}
