package com.server.app.service;

import com.server.app.dto.ShipperDto;
import com.server.app.dto.request.ShipperSaveRequest;
import com.server.app.dto.request.ShipperUpdateRequest;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.Shipper;

import java.util.List;

public interface ShipperService {

    GenericResponse add(ShipperSaveRequest request);

    GenericResponse update(ShipperUpdateRequest request);

    DataGenericResponse<ShipperDto> findShipperByShipperId(Long id);

    GenericResponse deleteShipperByShipperId(Long id);

    DataGenericResponse<List<ShipperDto>> findAllShippers();

    Shipper getShipper(Long shipperId);
}
