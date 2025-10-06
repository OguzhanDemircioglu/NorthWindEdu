package com.server.app.repository;

import com.server.app.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findSupplierBySupplierId(Long supplierId);

    void deleteSupplierBySupplierId(Long supplierId);

    Supplier getSupplierBySupplierId(Long supplierId);

    boolean existsSupplierBySupplierId(Long supplierId);

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE supplier_seq RESTART WITH 1", nativeQuery = true)
    void resetSupplierSequence();
}
