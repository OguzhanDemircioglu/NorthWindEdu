package com.server.app.service;

import com.server.app.dto.UsStateDto;
import com.server.app.dto.request.UsStateSaveRequest;
import com.server.app.dto.request.UsStateUpdateRequest;

import java.util.List;

public interface UsStateService {

    String add(UsStateSaveRequest request);

    UsStateDto update(UsStateUpdateRequest request);

    UsStateDto findStateByStateId(Short id);

    void deleteStateByStateId(Short id);

    List<UsStateDto> findAllStates();
}
