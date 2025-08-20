package com.server.app.repository;

import com.server.app.model.Shipper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipperRepository extends JpaRepository<Shipper, Long> {

    Optional<Shipper> findShipperByShipperId(Long shipperId);

    void deleteShipperByShipperId(Long shipperId);
}
