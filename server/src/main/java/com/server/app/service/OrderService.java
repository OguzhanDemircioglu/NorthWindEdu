package com.server.app.service;

import com.server.app.dto.request.OrderSaveRequest;
import com.server.app.dto.request.OrderUpdateRequest;
import com.server.app.dto.OrderDto;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;

import java.util.List;

public interface OrderService {

    GenericResponse add(OrderSaveRequest request);

    GenericResponse update(OrderUpdateRequest request);

    DataGenericResponse<OrderDto> findOrderByOrderId(Short orderId);

    GenericResponse deleteOrderByOrderId(Short orderId);

    DataGenericResponse<List<OrderDto>> findAllOrders();
}
