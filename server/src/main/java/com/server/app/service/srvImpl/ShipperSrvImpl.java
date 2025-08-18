package com.server.app.service.srvImpl;

import com.server.app.dto.ShipperDto;
import com.server.app.dto.request.ShipperSaveRequest;
import com.server.app.dto.request.ShipperUpdateRequest;
import com.server.app.model.Shipper;
import com.server.app.repository.ShipperRepository;
import com.server.app.service.ShipperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShipperSrvImpl implements ShipperService {

    private final ShipperRepository repository;

    @Override
    public String add(ShipperSaveRequest request) {
        try {
            repository.save(
                    Shipper.builder()
                            .companyName(request.getCompanyName())
                            .phone(request.getPhone())
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace(); // İstersen log.error(...) ile değiştirilebilir
            return "İşlem Başarısız";
        }
        return "İşlem Başarılı";
    }

    @Override
    public ShipperDto update(ShipperUpdateRequest request) {
        try {
            Optional<Shipper> shipper = repository.findShipperByShipperId(request.getShipperId());
            if (shipper.isEmpty()) {
                throw new RuntimeException("Update Edilecek Kayıt Bulunamadı");
            }

            shipper.get().setCompanyName(request.getCompanyName());
            shipper.get().setPhone(request.getPhone());

            repository.save(shipper.get());

            return shipperToShipperDtoMapper(shipper.get());
        } catch (Exception e) {
            e.printStackTrace(); // İstersen log.error(...) ile değiştirilebilir
            throw new RuntimeException("İşlem Başarısız");
        }
    }

    @Override
    public ShipperDto findShipperByShipperId(Long id) {
        Optional<Shipper> shipper = repository.findShipperByShipperId(id);
        if (shipper.isEmpty()) {
            throw new RuntimeException("Kayıt Bulunamadı");
        }
        return shipperToShipperDtoMapper(shipper.get());
    }

    @Override
    public void deleteShipperByShipperId(Long id) {
        repository.deleteShipperByShipperId(id);
    }

    @Override
    public List<ShipperDto> findAllShippers() {
        List<Shipper> list = repository.findAll();
        List<ShipperDto> result = new ArrayList<>();

        for (Shipper s : list) {
            ShipperDto dto = shipperToShipperDtoMapper(s);
            result.add(dto);
        }

        return result;
    }

    private ShipperDto shipperToShipperDtoMapper(Shipper s) {
        if (s == null) return null;

        ShipperDto dto = new ShipperDto();
        dto.setShipperId(s.getShipperId());
        dto.setCompanyName(s.getCompanyName());
        dto.setPhone(s.getPhone());
        return dto;
    }
}
