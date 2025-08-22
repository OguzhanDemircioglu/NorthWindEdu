package com.server.app.repository;

import com.server.app.model.Territory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TerritoryRepository extends JpaRepository<Territory, String> {

    Optional<Territory> findTerritoryByTerritoryId(String id);

    void deleteTerritoryByTerritoryId(String id);

    Territory getTerritoryByTerritoryId(String id);

    boolean existsTerritoryByTerritoryId(String id);
}
