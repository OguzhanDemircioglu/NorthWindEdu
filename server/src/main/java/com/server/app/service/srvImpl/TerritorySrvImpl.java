package com.server.app.service.srvImpl;

import com.google.common.base.Strings;
import com.server.app.dto.response.TerritoryDto;
import com.server.app.dto.request.territory.TerritorySaveRequest;
import com.server.app.dto.request.territory.TerritoryUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.TerritoryMapper;
import com.server.app.model.Territory;
import com.server.app.repository.TerritoryRepository;
import com.server.app.service.TerritoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TerritorySrvImpl implements TerritoryService {

    private final TerritoryRepository repository;
    private final TerritoryMapper mapper;

    @Override
    public GenericResponse add(TerritorySaveRequest request) {
        Territory territory = mapper.saveEntityFromRequest(request);

        Long maxId = repository.findMaxId();
        Long newId = (maxId == null) ? 1L : maxId + 1;
        territory.setTerritoryId(newId);

        BusinessRules.validate(checkTerritoryForGeneralValidations(territory));

        repository.save(territory);
        return new GenericResponse();
    }

    @Override
    public GenericResponse update(TerritoryUpdateRequest request) {
        Territory territory = mapper.toEntity(request);

        BusinessRules.validate(checkTerritoryForGeneralValidations(territory));

        repository.save(territory);
        return GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).build();
    }

    @Override
    public DataGenericResponse<TerritoryDto> findTerritoryByTerritoryId(Long id) {
        Optional<Territory> territory = repository.findTerritoryByTerritoryId(id);
        if (territory.isEmpty()) {
            throw new BusinessException(ResultMessages.TERRITORY_NOT_FOUND);
        }

        TerritoryDto dto = mapper.toDto(territory.get());

        return DataGenericResponse.<TerritoryDto>dataBuilder()
                .data(dto)
                .build();
    }

    @Override
    public GenericResponse deleteTerritoryByTerritoryId(Long id) {
        boolean isExist = repository.existsTerritoryByTerritoryId(id);
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }
        repository.deleteTerritoryByTerritoryId(id);

        return GenericResponse.builder().message(ResultMessages.RECORD_DELETED).build();
    }

    @Override
    public DataGenericResponse<List<TerritoryDto>> findAllTerritories() {
        List<Territory> territories = repository.findAll();
        List<TerritoryDto> dtos = territories.stream()
                .map(mapper::toDto)
                .toList();

        return DataGenericResponse.<List<TerritoryDto>>dataBuilder()
                .data(dtos)
                .build();
    }

    private String checkTerritoryForGeneralValidations(Territory request) {

        if(Strings.isNullOrEmpty(request.getTerritoryDescription())) {
            return ResultMessages.EMPTY_T_DESCRIPTION;
        }
        return null;
    }

    public Territory getTerritory(Long id) {
        return repository.getTerritoryByTerritoryId(id);
    }


}
