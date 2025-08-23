package com.server.app.mapper;

import com.server.app.dto.response.OrderDto;
import com.server.app.dto.request.order.OrderSaveRequest;
import com.server.app.dto.request.order.OrderUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.model.Customer;
import com.server.app.model.Employee;
import com.server.app.model.Order;
import com.server.app.model.Shipper;
import com.server.app.repository.OrderRepository;
import com.server.app.service.CustomerService;
import com.server.app.service.EmployeeService;
import com.server.app.service.ShipperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final ShipperService shipperService;

    public OrderDto toDto(Order request) {
        return OrderDto.builder()
                .orderId(request.getOrderId())
                .customerId(
                        Objects.isNull(request.getCustomer())
                                ? null
                                : request.getCustomer().getCustomerId())
                .employeeId(
                        Objects.isNull(request.getEmployee())
                                ? null
                                : request.getEmployee().getEmployeeId())
                .shipViaId(
                        Objects.isNull(request.getShipVia())
                                ? null
                                : request.getShipVia().getShipperId())
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

    public Order toEntity(OrderUpdateRequest request) {
        if (request.getOrderId() == null || request.getOrderId() == 0) {
            throw new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        boolean isExist = orderRepository.existsOrderByOrderId(Long.valueOf(request.getOrderId()));
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        Customer customer = customerService.getCustomer(request.getCustomerId());

        Employee employee = employeeService.getEmployee(request.getEmployeeId());
        if (Objects.isNull(employee)) {
            throw new BusinessException(ResultMessages.EMPLOYEE_NOT_FOUND);
        }

        Shipper shipper = shipperService.getShipper(request.getShipViaId());
        if (Objects.isNull(shipper)) {
            throw new BusinessException(ResultMessages.SHIPPER_NOT_FOUND);
        }

        return updateEntityFromRequest(request, customer, employee, shipper);
    }

    private Order updateEntityFromRequest(OrderUpdateRequest request, Customer customer, Employee employee, Shipper shipper) {
        return Order.builder()
                .orderId(Long.valueOf(request.getOrderId()))
                .customer(customer)
                .employee(employee)
                .shipVia(shipper)
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

    public Order saveEntityFromRequest(OrderSaveRequest request) {
        Customer customer = customerService.getCustomer(request.getCustomerId());

        Employee employee = employeeService.getEmployee(request.getEmployeeId());
        if (Objects.isNull(employee)) {
            throw new BusinessException(ResultMessages.EMPLOYEE_NOT_FOUND);
        }

        Shipper shipper = shipperService.getShipper(request.getShipViaId());
        if (Objects.isNull(shipper)) {
            throw new BusinessException(ResultMessages.SHIPPER_NOT_FOUND);
        }

        return Order.builder()
                .customer(customer)
                .employee(employee)
                .shipVia(shipper)
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
}
