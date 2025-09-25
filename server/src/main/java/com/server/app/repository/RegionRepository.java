package com.server.app.repository;

import com.server.app.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long>{

    Optional<Region> findRegionByRegionId(Long id);

    void deleteRegionByRegionId(Long id);

    boolean existsRegionByRegionId(Long id);

    Region getRegionByRegionId(Long id);

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE region_seq RESTART WITH 1", nativeQuery = true)
    void resetRegionSequence();
}
