package com.server.app.service;

import com.server.app.dto.request.order.OrderSaveRequest;
import com.server.app.dto.request.order.OrderUpdateRequest;
import com.server.app.dto.response.OrderDto;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;

import java.util.List;

public interface OrderService {

    GenericResponse add(OrderSaveRequest request);

    GenericResponse update(OrderUpdateRequest request);

    DataGenericResponse<OrderDto> findOrderByOrderId(Long orderId);

    GenericResponse deleteOrderByOrderId(Long orderId);

    DataGenericResponse<List<OrderDto>> findAllOrders();
}
