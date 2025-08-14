package com.server.app.service;

import com.server.app.dto.RegionDto;
import com.server.app.dto.request.RegionSaveRequest;
import com.server.app.dto.request.RegionUpdateRequest;

import java.util.List;

public interface RegionService {

    String add(RegionSaveRequest request);

    RegionDto update(RegionUpdateRequest request);

    RegionDto findRegionByRegionId(Short id);

    void deleteRegionByRegionId(Short id);

    List<RegionDto> findAllRegions();
}
