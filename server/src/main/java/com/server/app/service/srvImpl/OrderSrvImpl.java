package com.server.app.service.srvImpl;

import com.server.app.dto.OrderDto;
import com.server.app.dto.request.OrderSaveRequest;
import com.server.app.dto.request.OrderUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.mapper.OrderMapper;
import com.server.app.model.Order;
import com.server.app.repository.OrderRepository;
import com.server.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderSrvImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    @Override
    public String add(OrderSaveRequest request) {
        try {
            Order order = mapper.saveEntityFromRequest(request);

            BusinessRules.validate(
                    checkDateConsistency(order),
                    checkFreight(order.getFreight())
            );

            repository.save(order);
        } catch (BusinessException e) {
            log.error("Business validation failed for order add: customerId={}, employeeId={}, shipViaId={}",
                    request.getCustomerId(), request.getEmployeeId(), request.getShipViaId(), e);
            throw e;
        } catch (Exception e) {
            log.error("Order add failed", e);
            return ResultMessages.PROCESS_FAILED;
        }
        return ResultMessages.SUCCESS;
    }

    @Override
    public OrderDto update(OrderUpdateRequest request) {
        try {
            Order order = mapper.toEntity(request);

            BusinessRules.validate(
                    checkDateConsistency(order),
                    checkFreight(order.getFreight())
            );

            Order updated = repository.save(order);
            return mapper.toDto(updated);

        } catch (BusinessException e) {
            log.error("Business validation failed for order update: {}", request.getOrderId(), e);
            throw e;
        } catch (Exception e) {
            log.error("Order update failed for ID: {}", request.getOrderId(), e);
            throw new BusinessException(ResultMessages.PROCESS_FAILED + ": " + e.getMessage());
        }
    }

    @Override
    public OrderDto findOrderByOrderId(Short orderId) {
        Optional<Order> order = repository.findOrderByOrderId(orderId);
        if (order.isEmpty()) {
            throw new RuntimeException(ResultMessages.RECORD_NOT_FOUND);
        }
        return mapper.toDto(order.get());
    }

    @Override
    public void deleteOrderByOrderId(Short orderId) {
        repository.deleteOrderByOrderId(orderId);
    }

    @Override
    public List<OrderDto> findAllOrders() {
        List<Order> list = repository.findAll();
        List<OrderDto> result = new ArrayList<>();

        for (Order o : list) {
            OrderDto dto = mapper.toDto(o);
            result.add(dto);
        }
        return result;
    }


    private String checkDateConsistency(Order o) {
        if (o == null) return null;
        if (o.getOrderDate() != null && o.getRequiredDate() != null
                && o.getRequiredDate().isBefore(o.getOrderDate())) {
            return ResultMessages.ORDER_REQUIRED_BEFORE_ORDER_DATE;
        }
        if (o.getOrderDate() != null && o.getShippedDate() != null
                && o.getShippedDate().isBefore(o.getOrderDate())) {
            return ResultMessages.ORDER_SHIPPED_BEFORE_ORDER_DATE;
        }
        return null;
    }

    private String checkFreight(Float freight) {
        if (freight != null && freight < 0f) {
            return ResultMessages.NEGATIVE_FREIGHT;
        }
        return null;
    }
}
