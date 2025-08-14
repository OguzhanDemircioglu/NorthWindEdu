package com.server.app.service;

import com.server.app.dto.SupplierDto;
import com.server.app.dto.request.SupplierSaveRequest;
import com.server.app.dto.request.SupplierUpdateRequest;

import java.util.List;

public interface SupplierService {

    String add(SupplierSaveRequest request);

    SupplierDto update(SupplierUpdateRequest request);

    SupplierDto findSupplierBySupplierId(Short supplierId);

    void deleteSupplierBySupplierId(Short supplierId);

    List<SupplierDto> findAllSuppliers();
}
