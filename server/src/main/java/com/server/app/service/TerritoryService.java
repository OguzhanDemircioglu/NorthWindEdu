package com.server.app.service;

import com.server.app.dto.TerritoryDto;
import com.server.app.dto.request.TerritorySaveRequest;
import com.server.app.dto.request.TerritoryUpdateRequest;
import com.server.app.model.Territory;

import java.util.List;

public interface TerritoryService {

    String add(TerritorySaveRequest request);

    TerritoryDto update(TerritoryUpdateRequest request);

    TerritoryDto findTerritoryByTerritoryId(String id);

    void deleteTerritoryByTerritoryId(String id);

    List<TerritoryDto> findAllTerritories();

    Territory getTerritory(String id);
}