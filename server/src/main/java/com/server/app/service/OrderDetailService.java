package com.server.app.service;

import com.server.app.dto.request.orderDetail.OrderDetailSaveRequest;
import com.server.app.dto.request.orderDetail.OrderDetailUpdateRequest;
import com.server.app.dto.response.OrderDetailDto;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;

import java.util.List;

public interface OrderDetailService {
    GenericResponse add(OrderDetailSaveRequest request);

    GenericResponse update(OrderDetailUpdateRequest request);

    DataGenericResponse<OrderDetailDto> findOrderDetailById(Long orderId, Long productId);

    GenericResponse deleteOrderDetailById(Long orderId, Long productId);

    DataGenericResponse<List<OrderDetailDto>> findAllOrderDetails();
}
