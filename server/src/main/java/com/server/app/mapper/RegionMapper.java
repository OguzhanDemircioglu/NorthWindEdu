package com.server.app.mapper;

import com.server.app.dto.request.region.RegionSaveRequest;
import com.server.app.dto.request.region.RegionUpdateRequest;
import com.server.app.dto.response.RegionDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.model.Region;
import com.server.app.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegionMapper {
    private final RegionRepository repository;

    public RegionDto toDto(Region request) {
        return RegionDto.builder()
                .regionId(request.getRegionId())
                .regionDescription(request.getRegionDescription())
                .build();
    }

    public Region toEntity(RegionUpdateRequest request) {
        if (request.getRegionId() == null || request.getRegionId() == 0) {
            throw new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        boolean isExist = repository.existsById(request.getRegionId());
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        return updateEntityFromRequest(request);
    }

    private Region updateEntityFromRequest(RegionUpdateRequest request) {
        return Region.builder()
                .regionId(request.getRegionId())
                .regionDescription(request.getRegionDescription())
                .build();
    }

    public Region saveEntityFromRequest(RegionSaveRequest request) {
        return Region.builder()
                .regionDescription(request.getRegionDescription())
                .build();
    }
}
