package com.server.app.dto.request.employeeTerritory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTerritoryUpdateRequest {
    private Long employeeId;
    private String territoryId;
}
