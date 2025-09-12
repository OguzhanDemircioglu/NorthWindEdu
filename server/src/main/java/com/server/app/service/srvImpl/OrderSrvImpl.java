package com.server.app.service.srvImpl;

import com.server.app.dto.response.OrderDto;
import com.server.app.dto.request.order.OrderSaveRequest;
import com.server.app.dto.request.order.OrderUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.OrderMapper;
import com.server.app.model.Order;
import com.server.app.repository.OrderRepository;
import com.server.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderSrvImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    @Override
    public GenericResponse add(OrderSaveRequest request) {
        Order order = mapper.saveEntityFromRequest(request);

        BusinessRules.validate(
                checkOrderForGeneralValidations(order)
        );

        orderRepository.save(order);
        return new GenericResponse();
    }

    @Override
    public GenericResponse update(OrderUpdateRequest request) {
        Order order = mapper.toEntity(request);

        BusinessRules.validate(
                checkOrderForGeneralValidations(order)
        );

        orderRepository.save(order);
        return GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).build();
    }

    @Override
    public DataGenericResponse<OrderDto> findOrderByOrderId(Long orderId) {
        Optional<Order> order = orderRepository.findOrderByOrderId(orderId);
        if (order.isEmpty()) {
            throw new BusinessException(ResultMessages.ORDER_NOT_FOUND);
        }
        OrderDto dto = mapper.toDto(order.get());
        return DataGenericResponse.<OrderDto>dataBuilder()
                .data(dto)
                .build();
    }

    @Override
    public GenericResponse deleteOrderByOrderId(Long orderId) {
        boolean isExist = orderRepository.existsOrderByOrderId(orderId);
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }
        orderRepository.deleteOrderByOrderId(orderId);
        return GenericResponse.builder().message(ResultMessages.RECORD_DELETED).build();
    }

    @Override
    public DataGenericResponse<List<OrderDto>> findAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> dtos = orders.stream()
                .map(mapper::toDto)
                .toList();

        return DataGenericResponse.<List<OrderDto>>dataBuilder()
                .data(dtos)
                .build();
    }

    @Override
    public Order getOrder(Long id) {

        Order order = orderRepository.getOrderByOrderId(id);
        if (Objects.isNull(order)) {
            throw new BusinessException(ResultMessages.ORDER_NOT_FOUND);
        }

        return order;
    }

    private String checkOrderForGeneralValidations(Order request) {

        if (request.getFreight() != null && request.getFreight() < 0) {
            return ResultMessages.FREIGHT_NEGATIVE;
        }

        if (request.getShipName() != null && request.getShipName().length() > 40) {
            return ResultMessages.SHIP_NAME_OUT_OF_RANGE;
        }

        if (request.getShipAddress() != null && request.getShipAddress().length() > 60) {
            return ResultMessages.SHIP_ADDRESS_OUT_OF_RANGE;
        }

        if (request.getShipCity() != null && request.getShipCity().length() > 15) {
            return ResultMessages.SHIP_CITY_OUT_OF_RANGE;
        }

        if (request.getShipRegion() != null && request.getShipRegion().length() > 15) {
            return ResultMessages.SHIP_REGION_OUT_OF_RANGE;
        }

        if (request.getShipPostalCode() != null && request.getShipPostalCode().length() > 10) {
            return ResultMessages.SHIP_POSTAL_CODE_OUT_OF_RANGE;
        }

        if (request.getShipCountry() != null && request.getShipCountry().length() > 15) {
            return ResultMessages.SHIP_COUNTRY_OUT_OF_RANGE;
        }

        if (request.getOrderDate() != null && request.getRequiredDate() != null
                && request.getRequiredDate().isBefore(request.getOrderDate())) {
            return ResultMessages.REQUIRED_DATE_INVALID;
        }

        if (request.getOrderDate() != null && request.getShippedDate() != null
                && request.getShippedDate().isBefore(request.getOrderDate())) {
            return ResultMessages.SHIPPED_DATE_INVALID;
        }

        return null;
    }
}
