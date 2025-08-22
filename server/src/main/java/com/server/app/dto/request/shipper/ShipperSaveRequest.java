// File: src/main/java/com/server/app/dto/request/ShipperSaveRequest.java
package com.server.app.dto.request.shipper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipperSaveRequest {
    private String companyName;
    private String phone;
}
