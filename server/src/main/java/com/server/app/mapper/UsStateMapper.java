package com.server.app.mapper;

import com.server.app.dto.request.usState.UsStateSaveRequest;
import com.server.app.dto.request.usState.UsStateUpdateRequest;
import com.server.app.dto.response.UsStateDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.model.UsState;
import com.server.app.repository.UsStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsStateMapper {
    private final UsStateRepository repository;

    public UsStateDto toDto(UsState request) {
        return UsStateDto.builder()
                .stateId(request.getStateId())
                .stateAbbr(request.getStateAbbr())
                .stateName(request.getStateName())
                .stateRegion(request.getStateRegion())
                .build();
    }

    public UsState toEntity(UsStateUpdateRequest request) {
        if(request.getStateId() == null || request.getStateId() == 0) {
            throw new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        boolean isExist = repository.existsStateByStateId(request.getStateId());
        if(!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        return updateEntityFromRequest(request);
    }

    private UsState updateEntityFromRequest(UsStateUpdateRequest request) {
        return UsState.builder()
                .stateId(request.getStateId())
                .stateAbbr(request.getStateAbbr())
                .stateName(request.getStateName())
                .stateRegion(request.getStateRegion())
                .build();
    }

    public UsState saveEntityFromRequest(UsStateSaveRequest request) {
        return UsState.builder()
                .stateName(request.getStateName())
                .stateAbbr(request.getStateAbbr())
                .stateRegion(request.getStateRegion())
                .build();
    }
}
