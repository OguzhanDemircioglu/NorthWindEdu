package com.server.app.service.srvImpl;

import com.server.app.dto.UsStateDto;
import com.server.app.dto.request.UsStateSaveRequest;
import com.server.app.dto.request.UsStateUpdateRequest;
import com.server.app.model.UsState;
import com.server.app.repository.UsStateRepository;
import com.server.app.service.UsStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsStateSrvImpl implements UsStateService {

    private final UsStateRepository repository;

    @Override
    public String add(UsStateSaveRequest request) {
        try {
            repository.save(
                    UsState.builder()
                            .stateName(request.getStateName())
                            .stateAbbr(request.getStateAbbr())
                            .stateRegion(request.getStateRegion())
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "İşlem Başarısız";
        }
        return "İşlem Başarılı";
    }

    @Override
    public UsStateDto update(UsStateUpdateRequest request) {
        try {
            Optional<UsState> state = repository.findStateByStateId(request.getStateId());
            if (state.isEmpty()) {
                throw new RuntimeException("Update edilecek Kayıt Bulunamadı");
            }

            state.get().setStateName(request.getStateName());
            state.get().setStateAbbr(request.getStateAbbr());
            state.get().setStateRegion(request.getStateRegion());

            repository.save(state.get());

            return stateToStateDtoMapper(state.get());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("İşlem Başarısız");
        }
    }

    @Override
    public UsStateDto findStateByStateId(Long id) {
        Optional<UsState> state = repository.findStateByStateId(id);
        if (state.isEmpty()) {
            throw new RuntimeException("Kayıt Bulunamadı");
        }
        return stateToStateDtoMapper(state.get());
    }

    @Override
    public void deleteStateByStateId(Long id) { repository.deleteStateByStateId(id); }

    @Override
    public List<UsStateDto> findAllStates() {
        List<UsState> list = repository.findAll();
        List<UsStateDto> result = new ArrayList<>();

        for (UsState us : list) {
            UsStateDto dto = stateToStateDtoMapper(us);
            result.add(dto);
        }

        return result;
    }

    private UsStateDto stateToStateDtoMapper(UsState us) {
        if (us == null) {
            return null;
        }

        UsStateDto dto = new UsStateDto();
        dto.setStateId(us.getStateId());
        dto.setStateName(us.getStateName());
        dto.setStateAbbr(us.getStateAbbr());
        dto.setStateRegion(us.getStateRegion());

        return dto;
    }
}
