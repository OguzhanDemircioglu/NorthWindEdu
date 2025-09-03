package com.server.app.service.srvImpl;

import com.server.app.dto.request.order.OrderSaveRequest;
import com.server.app.dto.request.order.OrderUpdateRequest;
import com.server.app.dto.response.OrderDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.OrderMapper;
import com.server.app.model.*;
import com.server.app.repository.OrderRepository;
import com.server.app.service.CustomerService;
import com.server.app.service.EmployeeService;
import com.server.app.service.OrderDetailService;
import com.server.app.service.ShipperService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderSrvImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ShipperService shipperService;

    @Mock
    private OrderDetailService orderDetailService;

    @InjectMocks
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderSrvImpl orderSrv;

    OrderSaveRequest saveRequest= new OrderSaveRequest();
    OrderUpdateRequest updateRequest= new OrderUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setCustomerId("cus1");
        saveRequest.setEmployeeId(1L);

        updateRequest.setCustomerId("cus2");
        updateRequest.setEmployeeId(2L);

        orderMapper = new OrderMapper(orderRepository, customerService, employeeService, shipperService, orderDetailService);
        orderSrv = new OrderSrvImpl(orderRepository, orderMapper);
    }

    @AfterEach
    void tearDown() {
        saveRequest = null;
        updateRequest = null;
        orderMapper = null;
        orderSrv = null;
    }

    @Nested
    class add {

        @Test
        void isInvalidFreight() {
            saveRequest.setFreight(-1.0);

            when(employeeService.getEmployee(saveRequest.getEmployeeId())).thenReturn(new Employee());
            when(customerService.getCustomer(saveRequest.getCustomerId())).thenReturn(new Customer());
            when(shipperService.getShipper(saveRequest.getShipViaId())).thenReturn(new Shipper());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.FREIGHT_NEGATIVE);
        }

        @Test
        void isInvalidShipName() {
            saveRequest.setShipName("s".repeat(41));

            when(employeeService.getEmployee(saveRequest.getEmployeeId())).thenReturn(new Employee());
            when(customerService.getCustomer(saveRequest.getCustomerId())).thenReturn(new Customer());
            when(shipperService.getShipper(saveRequest.getShipViaId())).thenReturn(new Shipper());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.SHIP_NAME_OUT_OF_RANGE);
        }

        @Test
        void isInvalidAddress() {
            saveRequest.setShipAddress("a".repeat(61));

            when(employeeService.getEmployee(saveRequest.getEmployeeId())).thenReturn(new Employee());
            when(customerService.getCustomer(saveRequest.getCustomerId())).thenReturn(new Customer());
            when(shipperService.getShipper(saveRequest.getShipViaId())).thenReturn(new Shipper());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.SHIP_ADDRESS_OUT_OF_RANGE);
        }

        @Test
        void isInvalidCity() {
            saveRequest.setShipCity("c".repeat(16));

            when(employeeService.getEmployee(saveRequest.getEmployeeId())).thenReturn(new Employee());
            when(customerService.getCustomer(saveRequest.getCustomerId())).thenReturn(new Customer());
            when(shipperService.getShipper(saveRequest.getShipViaId())).thenReturn(new Shipper());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.SHIP_CITY_OUT_OF_RANGE);
        }

        @Test
        void isInvalidRegion() {
            saveRequest.setShipRegion("r".repeat(16));

            when(employeeService.getEmployee(saveRequest.getEmployeeId())).thenReturn(new Employee());
            when(customerService.getCustomer(saveRequest.getCustomerId())).thenReturn(new Customer());
            when(shipperService.getShipper(saveRequest.getShipViaId())).thenReturn(new Shipper());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.SHIP_REGION_OUT_OF_RANGE);
        }

        @Test
        void isInvalidPostalCode() {
            saveRequest.setShipPostalCode("p".repeat(11));

            when(employeeService.getEmployee(saveRequest.getEmployeeId())).thenReturn(new Employee());
            when(customerService.getCustomer(saveRequest.getCustomerId())).thenReturn(new Customer());
            when(shipperService.getShipper(saveRequest.getShipViaId())).thenReturn(new Shipper());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.SHIP_POSTAL_CODE_OUT_OF_RANGE);
        }

        @Test
        void isInvalidCountry() {
            saveRequest.setShipCountry("c".repeat(16));

            when(employeeService.getEmployee(saveRequest.getEmployeeId())).thenReturn(new Employee());
            when(customerService.getCustomer(saveRequest.getCustomerId())).thenReturn(new Customer());
            when(shipperService.getShipper(saveRequest.getShipViaId())).thenReturn(new Shipper());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.SHIP_COUNTRY_OUT_OF_RANGE);
        }

        @Test
        void isInvalidRequiredDate() {
            saveRequest.setOrderDate(LocalDate.of(2025, 8, 5));
            saveRequest.setRequiredDate(LocalDate.of(2025, 8, 1));

            when(employeeService.getEmployee(saveRequest.getEmployeeId())).thenReturn(new Employee());
            when(customerService.getCustomer(saveRequest.getCustomerId())).thenReturn(new Customer());
            when(shipperService.getShipper(saveRequest.getShipViaId())).thenReturn(new Shipper());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.REQUIRED_DATE_INVALID);
        }

        @Test
        void isInvalidShippedDate() {
            saveRequest.setOrderDate(LocalDate.of(2025, 8, 5));
            saveRequest.setShippedDate(LocalDate.of(2025, 8, 1));

            when(employeeService.getEmployee(saveRequest.getEmployeeId())).thenReturn(new Employee());
            when(customerService.getCustomer(saveRequest.getCustomerId())).thenReturn(new Customer());
            when(shipperService.getShipper(saveRequest.getShipViaId())).thenReturn(new Shipper());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.SHIPPED_DATE_INVALID);
        }

        @Test
        void isSuccess() {
            Employee employee = new Employee();
            employee.setEmployeeId(1L);
            Shipper shipper = new Shipper();
            shipper.setShipperId(1L);
            saveRequest.setShipViaId(1L);
            Customer customer = new Customer();
            customer.setCustomerId("cus1");

            when(employeeService.getEmployee(1L)).thenReturn(employee);
            when(shipperService.getShipper(saveRequest.getShipViaId())).thenReturn(shipper);
            when(customerService.getCustomer("cus1")).thenReturn(customer);

            when(orderRepository.save(any(Order.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = orderSrv.add(saveRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.SUCCESS);
        }
    }

    @Nested
    class update {

        @Test
        void isSuccess() {
            updateRequest.setOrderId(1L);
            Employee employee = new Employee();
            employee.setEmployeeId(2L);
            Shipper shipper = new Shipper();
            shipper.setShipperId(2L);
            updateRequest.setShipViaId(2L);
            Customer customer = new Customer();
            customer.setCustomerId("cus2");

            when(employeeService.getEmployee(2L)).thenReturn(employee);
            when(shipperService.getShipper(2L)).thenReturn(shipper);
            when(customerService.getCustomer("cus2")).thenReturn(customer);

            when(orderRepository.existsOrderByOrderId(updateRequest.getOrderId())).thenReturn(true);

            when((orderRepository.save(any(Order.class))))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = orderSrv.update(updateRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_UPDATED);
        }
    }

    @Nested
    class findOrderById {

        @Test
        void isOrderNotFound() {
            when(orderRepository.findOrderByOrderId(1L)).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderSrv.findOrderByOrderId(1L)
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.RECORD_NOT_FOUND);
        }
        @Test
        void isSuccess() {
            Order order = new Order();
            order.setOrderId(1L);

            when(orderRepository.findOrderByOrderId(order.getOrderId())).thenReturn(Optional.of(order));

            DataGenericResponse<OrderDto> response = orderSrv.findOrderByOrderId(order.getOrderId());

            assertThat(response).isNotNull();
            assertThat(response.getData().getOrderId()).isEqualTo(1L);
        }
    }

    @Nested
    class delete {

        @Test
        void isOrderNotFound() {
            when(orderRepository.existsOrderByOrderId(1L)).thenReturn(false);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderSrv.deleteOrderByOrderId(1L)
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.RECORD_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            when(orderRepository.existsOrderByOrderId(1L)).thenReturn(true);

            GenericResponse response = orderSrv.deleteOrderByOrderId(1L);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_DELETED);
        }
    }

    @Nested
    class findALlOrders {

        @Test
        void isSuccess() {
            Order order1 = new Order();
            order1.setOrderId(1L);

            Order order2 = new Order();
            order2.setOrderId(2L);

            when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

            DataGenericResponse<List<OrderDto>> response = orderSrv.findAllOrders();

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getData().size()).isEqualTo(2);
        }
    }
}