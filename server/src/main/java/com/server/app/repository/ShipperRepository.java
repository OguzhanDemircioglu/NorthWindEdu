package com.server.app.repository;

import com.server.app.model.Shipper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipperRepository extends JpaRepository<Shipper, Short> {

    Optional<Shipper> findShipperByShipperId(Short shipperId);

    void deleteShipperByShipperId(Short shipperId);
}
