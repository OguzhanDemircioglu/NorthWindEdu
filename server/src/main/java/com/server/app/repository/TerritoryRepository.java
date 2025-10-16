package com.server.app.repository;

import com.server.app.model.Territory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TerritoryRepository extends JpaRepository<Territory, Long> {

    Optional<Territory> findTerritoryByTerritoryId(Long id);

    void deleteTerritoryByTerritoryId(Long id);

    Territory getTerritoryByTerritoryId(Long id);

    boolean existsTerritoryByTerritoryId(Long id);
}
