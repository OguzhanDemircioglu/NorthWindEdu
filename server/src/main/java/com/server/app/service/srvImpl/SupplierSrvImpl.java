package com.server.app.service.srvImpl;

import com.server.app.dto.SupplierDto;
import com.server.app.dto.request.SupplierSaveRequest;
import com.server.app.dto.request.SupplierUpdateRequest;
import com.server.app.model.Supplier;
import com.server.app.repository.SupplierRepository;
import com.server.app.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        } catch (Exception e) {
            e.printStackTrace();
            return "İşlem Başarısız";
        }
        return "İşlem Başarılı";
    }

    @Override
    public SupplierDto update(SupplierUpdateRequest request) {
        SupplierDto result = new SupplierDto();

        Optional<Supplier> supplier = repository.findSupplierBySupplierId(request.getSupplierId());
        if (supplier.isEmpty()) {
            throw new RuntimeException("Update Edilecek Kayıt bulunamadı");
        }

        supplier.get().setCompanyName(request.getCompanyName());
        supplier.get().setContactName(request.getContactName());
        supplier.get().setContactTitle(request.getContactTitle());
        supplier.get().setAddress(request.getAddress());
        supplier.get().setCity(request.getCity());
        supplier.get().setRegion(request.getRegion());
        supplier.get().setPostalCode(request.getPostalCode());
        supplier.get().setCountry(request.getCountry());
        supplier.get().setPhone(request.getPhone());
        supplier.get().setFax(request.getFax());
        supplier.get().setHomepage(request.getHomepage());

        // DTO map
        result.setSupplierId(supplier.get().getSupplierId());
        result.setCompanyName(supplier.get().getCompanyName());
        result.setContactName(supplier.get().getContactName());
        result.setContactTitle(supplier.get().getContactTitle());
        result.setAddress(supplier.get().getAddress());
        result.setCity(supplier.get().getCity());
        result.setRegion(supplier.get().getRegion());
        result.setPostalCode(supplier.get().getPostalCode());
        result.setCountry(supplier.get().getCountry());
        result.setPhone(supplier.get().getPhone());
        result.setFax(supplier.get().getFax());
        result.setHomepage(supplier.get().getHomepage());

        repository.save(supplier.get());
        return result;
    }

    @Override
    public SupplierDto findSupplierBySupplierId(Short supplierId) {
        SupplierDto result = new SupplierDto();

        Optional<Supplier> supplier = repository.findSupplierBySupplierId(supplierId);
        if (supplier.isEmpty()) {
            throw new RuntimeException("Kayıt bulunamadı");
        }

        result.setSupplierId(supplier.get().getSupplierId());
        result.setCompanyName(supplier.get().getCompanyName());
        result.setContactName(supplier.get().getContactName());
        result.setContactTitle(supplier.get().getContactTitle());
        result.setAddress(supplier.get().getAddress());
        result.setCity(supplier.get().getCity());
        result.setRegion(supplier.get().getRegion());
        result.setPostalCode(supplier.get().getPostalCode());
        result.setCountry(supplier.get().getCountry());
        result.setPhone(supplier.get().getPhone());
        result.setFax(supplier.get().getFax());
        result.setHomepage(supplier.get().getHomepage());

        return result;
    }

    @Override
    public void deleteSupplierBySupplierId(Short supplierId) {
        repository.deleteSupplierBySupplierId(supplierId);
    }

    @Override
    public List<SupplierDto> findAllSuppliers() {
        List<Supplier> list = repository.findAll();
        List<SupplierDto> result = new ArrayList<>();

        for (Supplier s : list) {
            SupplierDto dto = new SupplierDto();
            dto.setSupplierId(s.getSupplierId());
            dto.setCompanyName(s.getCompanyName());
            dto.setContactName(s.getContactName());
            dto.setContactTitle(s.getContactTitle());
            dto.setAddress(s.getAddress());
            dto.setRegion(s.getRegion());
            dto.setPostalCode(s.getPostalCode());
            dto.setCountry(s.getCountry());
            dto.setPhone(s.getPhone());
            dto.setFax(s.getFax());
            dto.setHomepage(s.getHomepage());
            result.add(dto);
        }

        return result;
    }
}
