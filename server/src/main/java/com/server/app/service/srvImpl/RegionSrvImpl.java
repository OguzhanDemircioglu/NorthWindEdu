package com.server.app.service.srvImpl;

import com.server.app.dto.RegionDto;
import com.server.app.dto.request.RegionSaveRequest;
import com.server.app.dto.request.RegionUpdateRequest;
import com.server.app.model.Region;
import com.server.app.repository.RegionRepository;
import com.server.app.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionSrvImpl implements RegionService {

    private final RegionRepository repository;

    @Override
    public String add(RegionSaveRequest request) {
        try {
            repository.save(
                    Region.builder()
                            .regionDescription(request.getRegionDescription())
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "İşlem Başarısız";
        }
        return "İşlem Başarılı";
    }

    @Override
    public RegionDto update(RegionUpdateRequest request) {
        try {
            Optional<Region> region = repository.findRegionByRegionId(request.getRegionId());
            if (region.isEmpty()) {
                throw new RuntimeException("Update Edilecek Kayıt Bulunamadı");
            }

            region.get().setRegionDescription(request.getRegionDescription());

            repository.save(region.get());

            return regionToRegionDtoMapper(region.get());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("İşlem Başarısız");
        }
    }

    @Override
    public RegionDto findRegionByRegionId(Long id) {
        Optional<Region> region = repository.findRegionByRegionId(id);
        if (region.isEmpty()) {
            throw new RuntimeException("Kayıt Bulunamadı");
        }

        return regionToRegionDtoMapper(region.get());
    }

    @Override
    public void deleteRegionByRegionId(Long id) { repository.deleteRegionByRegionId(id); }

    @Override
    public List<RegionDto> findAllRegions() {
        List<Region> list = repository.findAll();
        List<RegionDto> result = new ArrayList<>();

        for (Region r : list) {
            RegionDto dto = regionToRegionDtoMapper(r);
            result.add(dto);
        }

        return result;
    }

    private RegionDto regionToRegionDtoMapper(Region r) {
        if (r == null) {
            return null;
        }

        RegionDto dto = new RegionDto();
        dto.setRegionId(r.getRegionId());
        dto.setRegionDescription(r.getRegionDescription());

        return dto;
    }
}
