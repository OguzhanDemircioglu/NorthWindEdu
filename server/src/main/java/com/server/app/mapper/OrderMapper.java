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
import com.server.app.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    // Sadece kendi repository'si (EmployeeMapper ile aynı desen)
    private final OrderRepository repository;

    // --- Entity -> DTO ---
    public OrderDto toDto(Order request) {
        if (request == null) return null;

        return OrderDto.builder()
                .orderId(request.getOrderId())
                .customerId(request.getCustomer() != null ? request.getCustomer().getCustomerId() : null)
                .employeeId(request.getEmployee() != null ? request.getEmployee().getEmployeeId() : null)
                .shipViaId(request.getShipVia() != null ? request.getShipVia().getShipperId() : null)
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

    // --- UpdateRequest -> Entity ---
    public Order toEntity(OrderUpdateRequest request) {
        Order existing = repository.findOrderByOrderId(request.getOrderId())
                .orElseThrow(() -> new BusinessException(ResultMessages.RECORD_NOT_FOUND));
        return updateEntityFromRequest(request, existing);
    }

    private Order updateEntityFromRequest(OrderUpdateRequest r, Order e) {
        // FK referanslarını yalnızca ID set ederek oluştur (DB sorgusu yok)
        Customer customer = null;
        if (r.getCustomerId() != null) {
            customer = new Customer();
            customer.setCustomerId(r.getCustomerId());
        }

        Employee employee = null;
        if (r.getEmployeeId() != null) {
            employee = new Employee();
            employee.setEmployeeId(r.getEmployeeId());
        }

        Shipper shipper = null;
        if (r.getShipViaId() != null) {
            shipper = new Shipper();
            shipper.setShipperId(r.getShipViaId());
        }

        return Order.builder()
                .orderId(e.getOrderId()) // PK sabit
                .customer(customer)
                .employee(employee)
                .shipVia(shipper)
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

    // --- SaveRequest -> Entity ---
    public Order saveEntityFromRequest(OrderSaveRequest r) {
        Customer customer = null;
        if (r.getCustomerId() != null) {
            customer = new Customer();
            customer.setCustomerId(r.getCustomerId());
        }

        Employee employee = null;
        if (r.getEmployeeId() != null) {
            employee = new Employee();
            employee.setEmployeeId(r.getEmployeeId());
        }

        Shipper shipper = null;
        if (r.getShipViaId() != null) {
            shipper = new Shipper();
            shipper.setShipperId(r.getShipViaId());
        }

        return Order.builder()
                .customer(customer)
                .employee(employee)
                .shipVia(shipper)
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
}
