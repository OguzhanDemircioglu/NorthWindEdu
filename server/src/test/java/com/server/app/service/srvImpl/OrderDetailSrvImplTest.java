package com.server.app.service.srvImpl;

import com.server.app.dto.request.orderDetail.OrderDetailSaveRequest;
import com.server.app.dto.request.orderDetail.OrderDetailUpdateRequest;
import com.server.app.dto.response.OrderDetailDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.OrderDetailMapper;
import com.server.app.model.Order;
import com.server.app.model.OrderDetail;
import com.server.app.model.Product;
import com.server.app.model.embedded.OrderDetailId;
import com.server.app.repository.OrderDetailRepository;
import com.server.app.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderDetailSrvImplTest {

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderDetailMapper orderDetailMapper;

    @InjectMocks
    private OrderDetailSrvImpl orderDetailSrv;

    OrderDetailSaveRequest saveRequest = new OrderDetailSaveRequest();
    OrderDetailUpdateRequest updateRequest = new OrderDetailUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setOrderId(1L);
        saveRequest.setProductId(1L);
        saveRequest.setUnitPrice(1.0);
        saveRequest.setQuantity(1L);
        saveRequest.setDiscount(0.0);

        updateRequest.setOrderId(1L);
        updateRequest.setProductId(1L);
        updateRequest.setUnitPrice(2.0);
        updateRequest.setQuantity(2L);
        updateRequest.setDiscount(1.0);

        orderDetailMapper = new OrderDetailMapper(orderDetailRepository, orderService, productService);
        orderDetailSrv = new OrderDetailSrvImpl(orderDetailMapper, orderDetailRepository);
    }

    @AfterEach
    void tearDown() {
        saveRequest = null;
        updateRequest = null;
        orderDetailMapper = null;
        orderDetailSrv = null;
    }

    @Nested
    class add {

        @Test
        void isEmptyOrderId() {
            saveRequest.setOrderId(null);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderDetailSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        @Test
        void isEmptyProductId() {
            saveRequest.setProductId(null);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderDetailSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        @Test
        void isEmptyQuantity() {
            saveRequest.setQuantity(null);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderDetailSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.OD_QUANTITY_EMPTY);
        }

        @Test
        void isEmptyDiscount() {
            saveRequest.setDiscount(null);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderDetailSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.OD_DISCOUNT_EMPTY);
        }

        @Test
        void isEmptyUnitPrice() {
            saveRequest.setUnitPrice(null);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderDetailSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.OD_UNIT_PRICE_EMPTY);
        }

        @Test
        void isInvalidQuantity() {
            saveRequest.setQuantity(-1L);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderDetailSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.OD_QUANTITY_NEGATIVE);
        }

        @Test
        void isInvalidUnitPrice() {
            saveRequest.setUnitPrice(-1.0);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderDetailSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.OD_UNIT_PRICE_NEGATIVE);
        }

        @Test
        void isInvalidDiscount() {
            saveRequest.setDiscount(2.0);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderDetailSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.OD_DISCOUNT_OUT_OF_RANGE);
        }

        @Test
        void isSuccess() {
            when(orderService.getOrder(saveRequest.getOrderId())).thenReturn(new Order());
            when(productService.getProduct(saveRequest.getProductId())).thenReturn(new Product());

            GenericResponse response = orderDetailSrv.add(saveRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.SUCCESS);
        }
    }

    @Nested
    class update {

        @Test
        void isOrderDetailNotFound() {
            OrderDetailId orderDetailId = new OrderDetailId(updateRequest.getOrderId(), updateRequest.getProductId());
            when(orderDetailRepository.findOrderDetailById(orderDetailId)).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderDetailSrv.findOrderDetailById(updateRequest.getOrderId(), updateRequest.getProductId())
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.RECORD_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            OrderDetailId orderDetailId = new OrderDetailId(updateRequest.getOrderId(), updateRequest.getProductId());
            when(orderDetailRepository.existsOrderDetailById(orderDetailId)).thenReturn(true);

            when(orderService.getOrder(updateRequest.getOrderId())).thenReturn(new Order());
            when(productService.getProduct(updateRequest.getProductId())).thenReturn(new Product());

            when(orderDetailRepository.save(any(OrderDetail.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = orderDetailSrv.update(updateRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_UPDATED);
        }
    }

    @Nested
    class findOrderDetailById {

        @Test
        void isSuccess() {
            Order order = new Order();
            order.setOrderId(1L);

            Product product = new Product();
            product.setProductId(1L);

            OrderDetailId orderDetailId = new OrderDetailId(1L, 1L);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId(orderDetailId);
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);

            when(orderDetailRepository.findOrderDetailById(orderDetailId)).thenReturn(Optional.of(orderDetail));

            DataGenericResponse<OrderDetailDto> response = orderDetailSrv.findOrderDetailById(1L, 1L);

            assertThat(response).isNotNull();
            assertThat(response.getData().getOrderId()).isEqualTo(1L);
            assertThat(response.getData().getProductId()).isEqualTo(1L);
        }
    }

    @Nested
    class delete {

        @Test
        void isOrderDetailNotFound() {
            OrderDetailId orderDetailId = new OrderDetailId(1L, 1L);

            when(orderDetailRepository.existsOrderDetailById(orderDetailId)).thenReturn(false);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> orderDetailSrv.deleteOrderDetailById(1L, 1L)
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.RECORD_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            OrderDetailId orderDetailId = new OrderDetailId(1L, 1L);

            when(orderDetailRepository.existsOrderDetailById(orderDetailId)).thenReturn(true);

            GenericResponse response = orderDetailSrv.deleteOrderDetailById(1L, 1L);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_DELETED);
        }
    }

    @Nested
    class findAllOrderDetails {

        @Test
        void isSuccess() {
            OrderDetail orderDetail1 = new OrderDetail();
            OrderDetail orderDetail2 = new OrderDetail();

            when(orderDetailRepository.findAll()).thenReturn(List.of(orderDetail1, orderDetail2));

            DataGenericResponse<List<OrderDetailDto>> response = orderDetailSrv.findAllOrderDetails();

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getData().size()).isEqualTo(2);
        }
    }
}