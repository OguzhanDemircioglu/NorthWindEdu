package com.server.app.repository;

import com.server.app.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findSupplierBySupplierId(Long supplierId);

    void deleteSupplierBySupplierId(Long supplierId);

    Supplier getSupplierBySupplierId(Long supplierId);

    boolean existsSupplierBySupplierId(Long supplierId);
}
