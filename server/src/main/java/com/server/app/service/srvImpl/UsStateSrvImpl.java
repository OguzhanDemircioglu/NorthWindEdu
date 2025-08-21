package com.server.app.service.srvImpl;

import com.server.app.dto.response.UsStateDto;
import com.server.app.dto.request.usState.UsStateSaveRequest;
import com.server.app.dto.request.usState.UsStateUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.UsStateMapper;
import com.server.app.model.UsState;
import com.server.app.repository.UsStateRepository;
import com.server.app.service.UsStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsStateSrvImpl implements UsStateService {

    private final UsStateRepository repository;
    private final UsStateMapper mapper;

    @Override
    public GenericResponse add(UsStateSaveRequest request) {
        UsState usState = mapper.saveEntityFromRequest(request);

        BusinessRules.validate(checkUsStateForGeneralValidations(usState));

        repository.save(usState);
        return new GenericResponse();
    }

    @Override
    public GenericResponse update(UsStateUpdateRequest request) {
        UsState usState = mapper.toEntity(request);

        BusinessRules.validate(checkUsStateForGeneralValidations(usState));

        repository.save(usState);
        return GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).build();
    }

    @Override
    public DataGenericResponse<UsStateDto> findStateByStateId(Long id) {
        Optional<UsState> state = repository.findStateByStateId(id);
        if (state.isEmpty()) {
            throw new RuntimeException(ResultMessages.STATE_NOT_FOUND);
        }

        UsStateDto dto = mapper.toDto(state.get());

        return DataGenericResponse.<UsStateDto>dataBuilder()
                .data(dto)
                .build();
    }

    @Override
    public GenericResponse deleteStateByStateId(Long id) {
        boolean isExist = repository.existsStateByStateId(id);
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        repository.deleteStateByStateId(id);

        return GenericResponse.builder().message(ResultMessages.RECORD_DELETED).build();
    }

    @Override
    public DataGenericResponse<List<UsStateDto>> findAllStates() {
        List<UsState> states = repository.findAll();
        List<UsStateDto> dtos = states.stream()
                .map(mapper::toDto)
                .toList();

        return DataGenericResponse.<List<UsStateDto>>dataBuilder()
                .data(dtos)
                .build();
    }

    private String checkUsStateForGeneralValidations(UsState request) {
        if (request.getStateId() == null || request.getStateId() == 0) {
            return ResultMessages.ID_IS_NOT_DELIVERED;
        }
        return null;
    }
}
