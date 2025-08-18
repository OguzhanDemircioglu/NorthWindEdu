package com.server.app.service;

import com.server.app.dto.ShipperDto;
import com.server.app.dto.request.ShipperSaveRequest;
import com.server.app.dto.request.ShipperUpdateRequest;
import com.server.app.model.Shipper;

import java.util.List;

public interface ShipperService {

    String add(ShipperSaveRequest request);

    ShipperDto update(ShipperUpdateRequest request);

    ShipperDto findShipperByShipperId(Long id);

    boolean existsByShipperId(Long shipperId);

    void deleteShipperByShipperId(Long id);

    List<ShipperDto> findAllShippers();

    Shipper getShipper(Long shipperId);
}
