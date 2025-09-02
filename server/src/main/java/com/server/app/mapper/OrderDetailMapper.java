package com.server.app.mapper;

import com.server.app.dto.request.orderDetail.OrderDetailSaveRequest;
import com.server.app.dto.request.orderDetail.OrderDetailUpdateRequest;
import com.server.app.dto.response.OrderDetailDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.model.Order;
import com.server.app.model.OrderDetail;
import com.server.app.model.embedded.OrderDetailId;
import com.server.app.model.Product;
import com.server.app.repository.OrderDetailRepository;
import com.server.app.service.OrderService;
import com.server.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OrderDetailMapper {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderService orderService;
    private final ProductService productService;

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

        Order order = orderService.getOrder(request.getOrderId());

        Product product = productService.getProduct(request.getProductId());

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

        Order order = orderService.getOrder(request.getOrderId());

        Product product = productService.getProduct(request.getProductId());

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
