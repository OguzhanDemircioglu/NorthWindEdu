package com.server.app.service;

import com.server.app.dto.response.RegionDto;
import com.server.app.dto.request.region.RegionSaveRequest;
import com.server.app.dto.request.region.RegionUpdateRequest;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.Region;

import javax.xml.crypto.Data;
import java.util.List;

public interface RegionService {

    GenericResponse add(RegionSaveRequest request);

    GenericResponse update(RegionUpdateRequest request);

    DataGenericResponse<RegionDto> findRegionByRegionId(Long id);

    GenericResponse deleteRegionByRegionId(Long id);

    DataGenericResponse<List<RegionDto>> findAllRegions();

    Region getRegion(Long regionId);
}
