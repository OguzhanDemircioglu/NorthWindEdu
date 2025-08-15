package com.server.app.service;

import com.server.app.dto.SupplierDto;
import com.server.app.dto.request.SupplierSaveRequest;
import com.server.app.dto.request.SupplierUpdateRequest;
import com.server.app.model.Supplier;

import java.util.List;

public interface SupplierService {

    String add(SupplierSaveRequest request);

    SupplierDto update(SupplierUpdateRequest request);

    SupplierDto findSupplierBySupplierId(Short supplierId);

    void deleteSupplierBySupplierId(Short supplierId);

    List<SupplierDto> findAllSuppliers();

    Supplier getSupplier(Short supplierId);

    boolean existsSupplierById(Short supplierId);
}
