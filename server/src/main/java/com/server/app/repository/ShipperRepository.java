package com.server.app.repository;

import com.server.app.model.Shipper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ShipperRepository extends JpaRepository<Shipper, Long> {

    Optional<Shipper> findShipperByShipperId(Long shipperId);

    void deleteShipperByShipperId(Long shipperId);

    Shipper getShipperByShipperId(Long shipperId);

    boolean existsShipperByShipperId(Long shipperId);

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE shipper_seq RESTART WITH 1", nativeQuery = true)
    void resetShipperSequence();
}
