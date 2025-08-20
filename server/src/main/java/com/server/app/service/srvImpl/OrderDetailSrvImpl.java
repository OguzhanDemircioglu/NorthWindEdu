package com.server.app.service.srvImpl;

import com.server.app.dto.request.OrderDetailSaveRequest;
import com.server.app.dto.request.OrderDetailUpdateRequest;
import com.server.app.dto.response.OrderDetailDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.OrderDetailMapper;
import com.server.app.model.OrderDetail;
import com.server.app.model.OrderDetailId;
import com.server.app.repository.OrderDetailRepository;
import com.server.app.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailSrvImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper mapper;

    @Override
    public GenericResponse add(OrderDetailSaveRequest request) {
        OrderDetail orderDetail = mapper.saveEntityFromRequest(request);

        BusinessRules.validate(
                checkOrderDetailForGeneralValidations(orderDetail),
                checkQuantityValidation(orderDetail.getQuantity()),
                checkUnitPriceValidation(orderDetail.getUnitPrice()),
                checkDiscountValidation(orderDetail.getDiscount())
        );

        orderDetailRepository.save(orderDetail);
        return new GenericResponse();
    }

    @Override
    public GenericResponse update(OrderDetailUpdateRequest request) {
        OrderDetail orderDetail = mapper.toEntity(request);

        BusinessRules.validate(
                checkOrderDetailForGeneralValidations(orderDetail),
                checkQuantityValidation(orderDetail.getQuantity()),
                checkUnitPriceValidation(orderDetail.getUnitPrice()),
                checkDiscountValidation(orderDetail.getDiscount())
        );

        orderDetailRepository.save(orderDetail);
        return GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).build();
    }

    @Override
    public DataGenericResponse<OrderDetailDto> findOrderDetailById(Long orderId, Long productId) {
        OrderDetailId id = new OrderDetailId(orderId, productId);
        Optional<OrderDetail> od = orderDetailRepository.findOrderDetailById(id);
        if (od.isEmpty()) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }
        OrderDetailDto dto = mapper.toDto(od.get());
        return DataGenericResponse.<OrderDetailDto>dataBuilder()
                .data(dto)
                .build();
    }

    @Override
    public GenericResponse deleteOrderDetailById(Long orderId, Long productId) {
        OrderDetailId id = new OrderDetailId(orderId, productId);
        boolean isExist = orderDetailRepository.existsOrderDetailById(id);
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }
        orderDetailRepository.deleteOrderDetailById(id);
        return GenericResponse.builder().message(ResultMessages.RECORD_DELETED).build();
    }

    @Override
    public DataGenericResponse<List<OrderDetailDto>> findAllOrderDetails() {
        List<OrderDetailDto> dtos = orderDetailRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();

        return DataGenericResponse.<List<OrderDetailDto>>dataBuilder()
                .data(dtos)
                .build();
    }


    private String checkOrderDetailForGeneralValidations(OrderDetail request) {
        if (request == null) {
            return ResultMessages.NULL_POINTER_REFERENCE;
        }
        // Mapper zaten Order/Product varlığını garanti ediyor; yine de korunmacı olalım
        if (request.getOrder() == null || request.getProduct() == null) {
            return ResultMessages.VALUES_NOT_MATCHED;
        }
        return null;
    }

    private String checkQuantityValidation(Long quantity) {
        if (quantity == null) {
            return ResultMessages.OD_QUANTITY_EMPTY;
        }
        if (quantity < 0) {
            return ResultMessages.OD_QUANTITY_NEGATIVE;
        }
        return null;
    }

    private String checkUnitPriceValidation(Double unitPrice) {
        if (unitPrice == null) {
            return ResultMessages.OD_UNIT_PRICE_EMPTY;
        }
        if (unitPrice < 0f) {
            return ResultMessages.OD_UNIT_PRICE_NEGATIVE;
        }
        return null;
    }

    private String checkDiscountValidation(Double discount) {
        if (discount == null) {
            return ResultMessages.OD_DISCOUNT_EMPTY;
        }
        if (discount < 0f || discount > 1f) {
            return ResultMessages.OD_DISCOUNT_OUT_OF_RANGE;
        }
        return null;
    }
}
