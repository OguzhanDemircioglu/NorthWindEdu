package com.server.app.repository;

import com.server.app.model.EmployeeTerritory;
import com.server.app.model.embedded.EmployeeTerritoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeTerritoryRepository extends JpaRepository<EmployeeTerritory, EmployeeTerritoryId> {

    Optional<EmployeeTerritory> findEmployeeTerritoryByEmployeeTerritoryId(EmployeeTerritoryId id);

    void deleteEmployeeTerritoryByEmployeeTerritoryId(EmployeeTerritoryId id);

    boolean existsByEmployeeTerritoryId_EmployeeIdAndEmployeeTerritoryId_TerritoryId(Long employeeId, Long territoryId);
}
