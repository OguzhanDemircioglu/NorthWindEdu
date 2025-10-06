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
import com.server.app.service.OrderDetailService;
import com.server.app.service.ShipperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final ShipperService shipperService;
    private final OrderDetailService orderDetailService;

    public OrderDto toDto(Order request) {

        return OrderDto.builder()
                .orderId(request.getOrderId())
                .customerId(request.getCustomer().getCustomerId())
                .employeeId(request.getEmployee().getEmployeeId())
                .shipViaId(request.getShipVia().getShipperId())
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
                .orderDetails(orderDetailService.getOrderDetails(request.getOrderId()))
                .build();
    }

    public Order toEntity(OrderUpdateRequest request) {
        if (request.getOrderId() == null || request.getOrderId() == 0) {
            throw new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        boolean isExist = orderRepository.existsOrderByOrderId(request.getOrderId());
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        return updateEntityFromRequest(request);
    }

    private Order updateEntityFromRequest(OrderUpdateRequest request) {
        Order order = orderRepository.getOrderByOrderId(request.getOrderId());

        Customer customer = customerService.getCustomer(request.getCustomerId());
        Employee employee = employeeService.getEmployee(request.getEmployeeId());
        Shipper shipper = shipperService.getShipper(request.getShipViaId());

        order.setCustomer(customer);
        order.setEmployee(employee);
        order.setShipVia(shipper);
        order.setOrderDate(request.getOrderDate());
        order.setRequiredDate(request.getRequiredDate());
        order.setShippedDate(request.getShippedDate());
        order.setFreight(request.getFreight());
        order.setShipName(request.getShipName());
        order.setShipAddress(request.getShipAddress());
        order.setShipCity(request.getShipCity());
        order.setShipRegion(request.getShipRegion());
        order.setShipPostalCode(request.getShipPostalCode());
        order.setShipCountry(request.getShipCountry());

        return orderRepository.save(order);
    }

    public Order saveEntityFromRequest(OrderSaveRequest request) {
        Customer customer = customerService.getCustomer(request.getCustomerId());

        Employee employee = employeeService.getEmployee(request.getEmployeeId());

        Shipper shipper = shipperService.getShipper(request.getShipViaId());

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
