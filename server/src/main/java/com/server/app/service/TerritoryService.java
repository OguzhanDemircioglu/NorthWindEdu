package com.server.app.service;

import com.server.app.dto.response.TerritoryDto;
import com.server.app.dto.request.territory.TerritorySaveRequest;
import com.server.app.dto.request.territory.TerritoryUpdateRequest;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.Territory;

import java.util.List;

public interface TerritoryService {

    GenericResponse add(TerritorySaveRequest request);

    GenericResponse update(TerritoryUpdateRequest request);

    DataGenericResponse<TerritoryDto> findTerritoryByTerritoryId(Long id);

    GenericResponse deleteTerritoryByTerritoryId(Long id);

    DataGenericResponse<List<TerritoryDto>> findAllTerritories();

    Territory getTerritory(Long id);
}