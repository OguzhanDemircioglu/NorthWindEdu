package com.server.app.service.srvImpl;

import com.google.common.base.Strings;
import com.server.app.dto.response.RegionDto;
import com.server.app.dto.request.region.RegionSaveRequest;
import com.server.app.dto.request.region.RegionUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.RegionMapper;
import com.server.app.model.Region;
import com.server.app.repository.RegionRepository;
import com.server.app.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionSrvImpl implements RegionService {

    private final RegionRepository repository;
    private final RegionMapper mapper;

    @Override
    public GenericResponse add(RegionSaveRequest request) {
        Region region = mapper.saveEntityFromRequest(request);

        BusinessRules.validate(checkRegionForDescription(region));

        repository.save(region);
        return new GenericResponse();
    }

    @Override
    public GenericResponse update(RegionUpdateRequest request) {
        Region region = mapper.toEntity(request);

        BusinessRules.validate(checkRegionForDescription(region));

        repository.save(region);
        return GenericResponse.builder()
                .message(ResultMessages.RECORD_UPDATED)
                .build();
    }

    @Override
    public DataGenericResponse<RegionDto> findRegionByRegionId(Long id) {
        Optional<Region> region = repository.findRegionByRegionId(id);
        if (region.isEmpty()) {
            throw new BusinessException(ResultMessages.REGION_NOT_FOUND);
        }

        RegionDto dto = mapper.toDto(region.get());

        return DataGenericResponse.<RegionDto>dataBuilder()
                .data(dto)
                .build();
    }

    @Override
    public GenericResponse deleteRegionByRegionId(Long id) {
        boolean isExist = repository.existsRegionByRegionId(id);
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        repository.deleteRegionByRegionId(id);

        return GenericResponse.builder().message(ResultMessages.RECORD_DELETED).build();
    }

    @Override
    public DataGenericResponse<List<RegionDto>> findAllRegions() {
        List<Region> regions = repository.findAll();
        List<RegionDto> dtos = regions.stream()
                .map(mapper::toDto)
                .toList();

        return DataGenericResponse.<List<RegionDto>>dataBuilder()
                .data(dtos)
                .build();
    }

    @Override
    public Region getRegion(Long regionId) {
        Region region = repository.getRegionByRegionId(regionId);
        if(Objects.isNull(region)) {
            throw new BusinessException(ResultMessages.REGION_NOT_FOUND);
        }
        return region;
    }

    private String checkRegionForDescription(Region request) {
        if(Strings.isNullOrEmpty(request.getRegionDescription())) {
            return ResultMessages.EMPTY_DESCRIPTION;
        }
        return null;
    }
}
