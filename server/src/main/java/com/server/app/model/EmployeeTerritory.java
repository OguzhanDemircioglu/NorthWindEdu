package com.server.app.model;

import com.server.app.model.embedded.EmployeeTerritoryId;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee_territories")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeTerritory {

    @EmbeddedId
    private EmployeeTerritoryId employeeTerritoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("territoryId")
    @JoinColumn(name = "territory_id")
    private Territory territory;
}
