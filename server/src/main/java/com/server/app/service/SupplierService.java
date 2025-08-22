package com.server.app.service;

import com.server.app.dto.response.SupplierDto;
import com.server.app.dto.request.supplier.SupplierSaveRequest;
import com.server.app.dto.request.supplier.SupplierUpdateRequest;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.Supplier;

import java.util.List;

public interface SupplierService {

    GenericResponse add(SupplierSaveRequest request);

    GenericResponse update(SupplierUpdateRequest request);

    DataGenericResponse<SupplierDto> findSupplierBySupplierId(Long supplierId);

    GenericResponse deleteSupplierBySupplierId(Long supplierId);

    DataGenericResponse<List<SupplierDto>> findAllSuppliers();

    Supplier getSupplier(Long supplierId);
}
