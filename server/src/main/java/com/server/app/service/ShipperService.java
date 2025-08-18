package com.server.app.service;

import com.server.app.dto.ShipperDto;
import com.server.app.dto.request.ShipperSaveRequest;
import com.server.app.dto.request.ShipperUpdateRequest;

import java.util.List;

public interface ShipperService {

    String add(ShipperSaveRequest request);

    ShipperDto update(ShipperUpdateRequest request);

    ShipperDto findShipperByShipperId(Short id);

    boolean existsByShipperId(Short shipperId);

    void deleteShipperByShipperId(Short id);

    List<ShipperDto> findAllShippers();
}
