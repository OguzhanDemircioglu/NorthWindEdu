package com.server.app.service;

import com.server.app.dto.OrderDto;
import com.server.app.dto.request.OrderSaveRequest;
import com.server.app.dto.request.OrderUpdateRequest;

import java.util.List;

public interface OrderService {

    String add(OrderSaveRequest request);

    OrderDto update(OrderUpdateRequest request);

    OrderDto findOrderByOrderId(Short orderId);

    void deleteOrderByOrderId(Short orderId);

    List<OrderDto> findAllOrders();
}
