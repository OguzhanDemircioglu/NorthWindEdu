package com.server.app.mapper;

import com.server.app.dto.request.territory.TerritorySaveRequest;
import com.server.app.dto.request.territory.TerritoryUpdateRequest;
import com.server.app.dto.response.TerritoryDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.model.Region;
import com.server.app.model.Territory;
import com.server.app.repository.TerritoryRepository;
import com.server.app.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TerritoryMapper {

    private final TerritoryRepository repository;
    private final RegionService regionService;

    public TerritoryDto toDto(Territory request) {
        return TerritoryDto.builder()
                .territoryId(request.getTerritoryId())
                .territoryDescription(request.getTerritoryDescription())
                .regionId(
                        Objects.isNull(request.getRegion())
                        ? null
                                : request.getRegion().getRegionId())
                .build();
    }

    public Territory toEntity(TerritoryUpdateRequest request) {
        if (request.getTerritoryId() == null || request.getTerritoryId().isEmpty()) {
            throw new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        boolean isExist = repository.existsTerritoryByTerritoryId(request.getTerritoryId());
        if (!isExist) {
            throw new BusinessException(ResultMessages.TERRITORY_NOT_FOUND);
        }

        return updateEntityFromRequest(request);
    }

    private Territory updateEntityFromRequest(TerritoryUpdateRequest request) {
        Region region = regionService.getRegion(request.getRegionId());
        if (Objects.isNull(region)) {
            throw new BusinessException(ResultMessages.REGION_NOT_FOUND);
        }

        return Territory.builder()
                .territoryId(request.getTerritoryId())
                .territoryDescription(request.getTerritoryDescription())
                .region(region)
                .build();
    }

    public Territory saveEntityFromRequest(TerritorySaveRequest request) {
        Region region = regionService.getRegion(request.getRegionId());
        if (Objects.isNull(region)) {
            throw new BusinessException(ResultMessages.REGION_NOT_FOUND);
        }

        return Territory.builder()
                .territoryId(request.getTerritoryId())
                .territoryDescription(request.getTerritoryDescription())
                .region(region)
                .build();
    }
}
