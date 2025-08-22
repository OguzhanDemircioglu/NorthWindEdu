package com.server.app.mapper;

import com.server.app.dto.ShipperDto;
import com.server.app.dto.request.ShipperSaveRequest;
import com.server.app.dto.request.ShipperUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.model.Shipper;
import com.server.app.repository.ShipperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShipperMapper {

    private final ShipperRepository repository;

    public ShipperDto toDto(Shipper request) {
        return ShipperDto.builder()
                .shipperId(request.getShipperId())
                .companyName(request.getCompanyName())
                .phone(request.getPhone())
                .build();
    }

    public Shipper toEntity(ShipperUpdateRequest request) {
        if (request.getShipperId() == null || request.getShipperId() == 0L) {
            throw new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        boolean isExist = repository.existsShipperByShipperId(request.getShipperId());

        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        return updateEntityFromRequest(request);
    }

    private Shipper updateEntityFromRequest(ShipperUpdateRequest request) {
        return Shipper.builder()
                .shipperId(request.getShipperId())
                .companyName(request.getCompanyName())
                .phone(request.getPhone())
                .build();
    }

    public Shipper saveEntityFromRequest(ShipperSaveRequest request) {
        return Shipper.builder()
                .companyName(request.getCompanyName())
                .phone(request.getPhone())
                .build();
    }
}
