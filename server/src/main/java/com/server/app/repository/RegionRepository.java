package com.server.app.repository;

import com.server.app.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Integer>{

    Optional<Region> findRegionByRegionId(Short id);

    void deleteRegionByRegionId(Short id);
}
