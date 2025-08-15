package com.server.app.mapper;

import com.server.app.dto.OrderDto;
import com.server.app.dto.request.OrderSaveRequest;
import com.server.app.dto.request.OrderUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.model.Customer;
import com.server.app.model.Employee;
import com.server.app.model.Order;
import com.server.app.model.Shipper;
import com.server.app.repository.CustomerRepository;
import com.server.app.repository.EmployeeRepository;
import com.server.app.repository.OrderRepository;
import com.server.app.repository.ShipperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final ShipperRepository shipperRepository;

    // --- Entity -> DTO ---
    public OrderDto toDto(Order o) {
        if (o == null) return null;

        return OrderDto.builder()
                .orderId(o.getOrderId())
                .customerId(o.getCustomer() != null ? o.getCustomer().getCustomerId() : null)
                .employeeId(o.getEmployee() != null ? o.getEmployee().getEmployeeId() : null)
                .shipViaId(o.getShipVia() != null ? o.getShipVia().getShipperId() : null)
                .orderDate(o.getOrderDate())
                .requiredDate(o.getRequiredDate())
                .shippedDate(o.getShippedDate())
                .freight(o.getFreight())
                .shipName(o.getShipName())
                .shipAddress(o.getShipAddress())
                .shipCity(o.getShipCity())
                .shipRegion(o.getShipRegion())
                .shipPostalCode(o.getShipPostalCode())
                .shipCountry(o.getShipCountry())
                .build();
    }

    public Order toEntity(OrderUpdateRequest request) {
        Order existing = orderRepository.findOrderByOrderId(request.getOrderId())
                .orElseThrow(() -> new BusinessException(ResultMessages.RECORD_NOT_FOUND));
        return updateEntityFromRequest(request, existing);
    }

    public Order saveEntityFromRequest(OrderSaveRequest request) {
        return Order.builder()
                .customer(resolveCustomer(request.getCustomerId()))
                .employee(resolveEmployee(request.getEmployeeId()))
                .shipVia(resolveShipper(request.getShipViaId()))
                .orderDate(request.getOrderDate())
                .requiredDate(request.getRequiredDate())
                .shippedDate(request.getShippedDate())
                .freight(request.getFreight())
                .shipName(request.getShipName())
                .shipAddress(request.getShipAddress())
                .shipCity(request.getShipCity())
                .shipRegion(request.getShipRegion())
                .shipPostalCode(request.getShipPostalCode())
                .shipCountry(request.getShipCountry())
                .build();
    }

    private Order updateEntityFromRequest(OrderUpdateRequest r, Order e) {
        return Order.builder()
                .orderId(e.getOrderId()) // PK sabit
                .customer(resolveCustomer(r.getCustomerId()))
                .employee(resolveEmployee(r.getEmployeeId()))
                .shipVia(resolveShipper(r.getShipViaId()))
                .orderDate(r.getOrderDate())
                .requiredDate(r.getRequiredDate())
                .shippedDate(r.getShippedDate())
                .freight(r.getFreight())
                .shipName(r.getShipName())
                .shipAddress(r.getShipAddress())
                .shipCity(r.getShipCity())
                .shipRegion(r.getShipRegion())
                .shipPostalCode(r.getShipPostalCode())
                .shipCountry(r.getShipCountry())
                .build();
    }

    private Customer resolveCustomer(String customerId) {
        if (customerId == null) return null;
        return customerRepository.findCustomerByCustomerId(customerId)
                .orElseThrow(() -> new BusinessException(ResultMessages.RECORD_NOT_FOUND));
    }

    private Employee resolveEmployee(Integer employeeId) {
        if (employeeId == null) return null;
        return employeeRepository.findEmployeeByEmployeeId(employeeId)
                .orElseThrow(() -> new BusinessException(ResultMessages.RECORD_NOT_FOUND));
    }

    private Shipper resolveShipper(Short shipperId) {
        if (shipperId == null) return null;
        return shipperRepository.findShipperByShipperId(shipperId)
                .orElseThrow(() -> new BusinessException(ResultMessages.RECORD_NOT_FOUND));
    }
}
