package com.server.app.service;

import com.server.app.dto.response.UsStateDto;
import com.server.app.dto.request.usState.UsStateSaveRequest;
import com.server.app.dto.request.usState.UsStateUpdateRequest;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;

import java.util.List;

public interface UsStateService {

    GenericResponse add(UsStateSaveRequest request);

    GenericResponse update(UsStateUpdateRequest request);

    DataGenericResponse<UsStateDto> findStateByStateId(Long id);

    GenericResponse deleteStateByStateId(Long id);

    DataGenericResponse<List<UsStateDto>> findAllStates();
}
