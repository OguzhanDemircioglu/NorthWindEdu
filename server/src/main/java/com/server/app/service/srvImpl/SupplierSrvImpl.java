package com.server.app.service.srvImpl;

import com.server.app.dto.SupplierDto;
import com.server.app.dto.request.SupplierSaveRequest;
import com.server.app.dto.request.SupplierUpdateRequest;
import com.server.app.model.Supplier;
import com.server.app.repository.SupplierRepository;
import com.server.app.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierSrvImpl implements SupplierService {

    private final SupplierRepository repository;

    @Override
    public String add(SupplierSaveRequest request) {
        try {
            repository.save(
                    Supplier.builder()
                            .companyName(request.getCompanyName())
                            .contactName(request.getContactName())
                            .contactTitle(request.getContactTitle())
                            .address(request.getAddress())
                            .city(request.getCity())
                            .region(request.getRegion())
                            .postalCode(request.getPostalCode())
                            .country(request.getCountry())
                            .phone(request.getPhone())
                            .fax(request.getFax())
                            .homepage(request.getHomepage())
                            .build()
            );
            return "İşlem Başarılı";
        } catch (Exception e) {
            e.printStackTrace(); // prod'da log.error(...) tercih edilir
            return "İşlem Başarısız";
        }
    }

    @Override
    public SupplierDto update(SupplierUpdateRequest request) {
        Optional<Supplier> supplierOpt = repository.findSupplierBySupplierId(request.getSupplierId());
        if (supplierOpt.isEmpty()) {
            throw new RuntimeException("Update Edilecek Kayıt bulunamadı");
        }

        Supplier s = supplierOpt.get();
        s.setCompanyName(request.getCompanyName());
        s.setContactName(request.getContactName());
        s.setContactTitle(request.getContactTitle());
        s.setAddress(request.getAddress());
        s.setCity(request.getCity());
        s.setRegion(request.getRegion());
        s.setPostalCode(request.getPostalCode());
        s.setCountry(request.getCountry());
        s.setPhone(request.getPhone());
        s.setFax(request.getFax());
        s.setHomepage(request.getHomepage());

        repository.save(s);
        return toDto(s);
    }

    @Override
    public SupplierDto findSupplierBySupplierId(Short supplierId) {
        return repository.findSupplierBySupplierId(supplierId)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Kayıt bulunamadı"));
    }

    @Override
    public void deleteSupplierBySupplierId(Short supplierId) {
        repository.deleteSupplierBySupplierId(supplierId);
    }

    @Override
    public List<SupplierDto> findAllSuppliers() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Supplier getSupplier(Short supplierId) {
        return repository.getSupplierBySupplierId(supplierId);
    }

    private SupplierDto toDto(Supplier s) {
        if (s == null) return null;
        SupplierDto dto = new SupplierDto();
        dto.setSupplierId(s.getSupplierId());
        dto.setCompanyName(s.getCompanyName());
        dto.setContactName(s.getContactName());
        dto.setContactTitle(s.getContactTitle());
        dto.setAddress(s.getAddress());
        dto.setCity(s.getCity());
        dto.setRegion(s.getRegion());
        dto.setPostalCode(s.getPostalCode());
        dto.setCountry(s.getCountry());
        dto.setPhone(s.getPhone());
        dto.setFax(s.getFax());
        dto.setHomepage(s.getHomepage());
        return dto;
    }
}
