package com.server.app.mapper;

import com.server.app.dto.request.supplier.SupplierSaveRequest;
import com.server.app.dto.request.supplier.SupplierUpdateRequest;
import com.server.app.dto.response.SupplierDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.model.Supplier;
import com.server.app.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupplierMapper {
    private final SupplierRepository repository;

    public SupplierDto toDto(Supplier request) {
        return SupplierDto.builder()
                .supplierId(request.getSupplierId())
                .region(request.getRegion())
                .address(request.getAddress())
                .fax(request.getFax())
                .city(request.getCity())
                .phone(request.getPhone())
                .country(request.getCountry())
                .companyName(request.getCompanyName())
                .contactName(request.getContactName())
                .contactTitle(request.getContactTitle())
                .homepage(request.getHomepage())
                .postalCode(request.getPostalCode())
                .build();
    }

    public Supplier toEntity(SupplierUpdateRequest request) {
        if (request.getSupplierId() == null || request.getSupplierId() == 0) {
            throw new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        boolean isExist = repository.existsSupplierBySupplierId(request.getSupplierId());
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        return updateEntityFromRequest(request);
    }

    private Supplier updateEntityFromRequest(SupplierUpdateRequest request) {
        return Supplier.builder()
                .supplierId(request.getSupplierId())
                .region(request.getRegion())
                .address(request.getAddress())
                .fax(request.getFax())
                .city(request.getCity())
                .phone(request.getPhone())
                .country(request.getCountry())
                .companyName(request.getCompanyName())
                .contactName(request.getContactName())
                .contactTitle(request.getContactTitle())
                .homepage(request.getHomepage())
                .postalCode(request.getPostalCode())
                .build();
    }

    public Supplier saveEntityFromRequest(SupplierSaveRequest request) {
        return Supplier.builder()
                .region(request.getRegion())
                .address(request.getAddress())
                .fax(request.getFax())
                .city(request.getCity())
                .phone(request.getPhone())
                .country(request.getCountry())
                .companyName(request.getCompanyName())
                .contactName(request.getContactName())
                .contactTitle(request.getContactTitle())
                .homepage(request.getHomepage())
                .postalCode(request.getPostalCode())
                .build();
    }
}
