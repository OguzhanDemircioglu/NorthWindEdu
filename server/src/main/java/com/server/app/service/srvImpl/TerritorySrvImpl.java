package com.server.app.service.srvImpl;

import com.server.app.dto.TerritoryDto;
import com.server.app.dto.request.TerritorySaveRequest;
import com.server.app.dto.request.TerritoryUpdateRequest;
import com.server.app.model.Region;
import com.server.app.model.Territory;
import com.server.app.repository.RegionRepository;
import com.server.app.repository.TerritoryRepository;
import com.server.app.service.TerritoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TerritorySrvImpl implements TerritoryService {

    private final TerritoryRepository repository;
    private final RegionRepository regionRepository;

    @Override
    public String add(TerritorySaveRequest request) {

        try {
            Region region = regionRepository.findRegionByRegionId(request.getRegionId())
                    .orElseThrow(() -> new RuntimeException("Region bulunamadı"));

            repository.save(
                    Territory.builder()
                            .territoryId(request.getTerritoryId())
                            .territoryDescription(request.getTerritoryDescription())
                            .region(region)
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "İşlem Başarısız";
        }
        return "İşlem Başarılı";
    }

    @Override
    public TerritoryDto update(TerritoryUpdateRequest request) {
        try {
            Region region = regionRepository.findRegionByRegionId(request.getRegionId())
                    .orElseThrow(() -> new RuntimeException("Region bulunamadı"));

            Optional<Territory> territory = repository.findTerritoryByTerritoryId(request.getTerritoryId());
            if (territory.isEmpty()) {
                throw new RuntimeException("Update Edilecek Kayıt Bulunamadı");
            }

            territory.get().setTerritoryDescription(request.getTerritoryDescription());
            territory.get().setRegion(region);

            repository.save(territory.get());

            return territoryToTerritoryDto(territory.get());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("İşlem Başarısız");
        }
    }

    @Override
    public TerritoryDto findTerritoryByTerritoryId(String id) {
        Optional<Territory> territory = repository.findTerritoryByTerritoryId(id);
        if (territory.isEmpty()) {
            throw new RuntimeException("Kayıt Bulunamadı");
        }

        return territoryToTerritoryDto(territory.get());
    }

    @Override
    public void deleteTerritoryByTerritoryId(String id) { repository.deleteTerritoryByTerritoryId(id); }

    @Override
    public List<TerritoryDto> findAllTerritories() {
        List<Territory> list = repository.findAll();
        List<TerritoryDto> result = new ArrayList<>();

        for (Territory t : list) {
            TerritoryDto dto = territoryToTerritoryDto(t);
            result.add(dto);
        }

        return result;
    }

    private TerritoryDto territoryToTerritoryDto(Territory t) {
        if (t == null) {
            return null;
        }

        TerritoryDto dto = new TerritoryDto();
        dto.setTerritoryId(t.getTerritoryId());
        dto.setTerritoryDescription(t.getTerritoryDescription());
        dto.setRegionId(t.getRegion().getRegionId());

        return dto;
    }


}
