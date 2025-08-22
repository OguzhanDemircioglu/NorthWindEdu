package com.server.app.service;

import com.server.app.dto.response.ShipperDto;
import com.server.app.dto.request.shipper.ShipperSaveRequest;
import com.server.app.dto.request.shipper.ShipperUpdateRequest;
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
