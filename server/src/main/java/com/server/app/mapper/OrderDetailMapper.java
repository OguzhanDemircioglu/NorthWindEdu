package com.server.app.mapper;

import com.server.app.dto.request.OrderDetailSaveRequest;
import com.server.app.dto.request.OrderDetailUpdateRequest;
import com.server.app.dto.response.OrderDetailDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.model.Order;
import com.server.app.model.OrderDetail;
import com.server.app.model.OrderDetailId;
import com.server.app.model.Product;
import com.server.app.repository.OrderDetailRepository;
import com.server.app.repository.OrderRepository;
import com.server.app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OrderDetailMapper {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderDetailDto toDto(OrderDetail request) {
        return OrderDetailDto.builder()
                .orderId(
                        Objects.isNull(request.getOrder())
                                ? null
                                : request.getOrder().getOrderId())
                .productId(
                        Objects.isNull(request.getProduct())
                                ? null
                                : request.getProduct().getProductId())
                .unitPrice(request.getUnitPrice())
                .quantity(request.getQuantity())   // Long
                .discount(request.getDiscount())
                .build();
    }

    public OrderDetail toEntity(OrderDetailUpdateRequest request) {
        if (request.getOrderId() == null || request.getOrderId() == 0L
                || request.getProductId() == null || request.getProductId() == 0L) {
            throw new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        OrderDetailId id = new OrderDetailId(request.getOrderId(), request.getProductId());
        boolean isExist = orderDetailRepository.existsOrderDetailById(id);
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new BusinessException(ResultMessages.RECORD_NOT_FOUND));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException(ResultMessages.RECORD_NOT_FOUND));

        return updateEntityFromRequest(request, order, product);
    }

    private OrderDetail updateEntityFromRequest(OrderDetailUpdateRequest request, Order order, Product product) {
        return OrderDetail.builder()
                .id(new OrderDetailId(request.getOrderId(), request.getProductId()))
                .order(order)
                .product(product)
                .unitPrice(request.getUnitPrice())
                .quantity(request.getQuantity())   // Long
                .discount(request.getDiscount())
                .build();
    }

    public OrderDetail saveEntityFromRequest(OrderDetailSaveRequest request) {
        if (request.getOrderId() == null || request.getOrderId() == 0L
                || request.getProductId() == null || request.getProductId() == 0L) {
            throw new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new BusinessException(ResultMessages.RECORD_NOT_FOUND));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException(ResultMessages.RECORD_NOT_FOUND));

        return OrderDetail.builder()
                .id(new OrderDetailId(request.getOrderId(), request.getProductId()))
                .order(order)
                .product(product)
                .unitPrice(request.getUnitPrice())
                .quantity(request.getQuantity())   // Long
                .discount(request.getDiscount())
                .build();
    }
}
