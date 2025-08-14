package com.server.app.repository;

import com.server.app.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Short> {
    Optional<Supplier> findSupplierBySupplierId(Short supplierId);

    void deleteSupplierBySupplierId(Short supplierId);
}
