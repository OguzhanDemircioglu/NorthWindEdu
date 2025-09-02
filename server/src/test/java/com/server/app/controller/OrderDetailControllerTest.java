package com.server.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.dto.request.orderDetail.OrderDetailSaveRequest;
import com.server.app.dto.request.orderDetail.OrderDetailUpdateRequest;
import com.server.app.dto.response.OrderDetailDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.embedded.OrderDetailId;
import com.server.app.service.JWTService;
import com.server.app.service.OrderDetailService;
import com.server.app.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderDetailController.class, excludeAutoConfiguration =  SecurityAutoConfiguration.class)
class OrderDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderDetailService orderDetailService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UserService userService;

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
    }

    @AfterEach
    void tearDown() {
        saveRequest = null;
        updateRequest = null;
        orderDetailService = null;
        jwtService = null;
        userService = null;
        objectMapper = null;
        mockMvc = null;
    }

    @Nested
    class add {

        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = new GenericResponse();

            BDDMockito.given(orderDetailService.add(Mockito.any(OrderDetailSaveRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(post("/api/order-details/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUCCESS)));

            verify(orderDetailService, times(1)).add(Mockito.any(OrderDetailSaveRequest.class));
        }

        @Test
        void isOrderNotFound() throws Exception {
            doThrow(new BusinessException(ResultMessages.ORDER_NOT_FOUND))
                    .when(orderDetailService).add(Mockito.any(OrderDetailSaveRequest.class));

            mockMvc.perform(post("/api/order-details/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new OrderDetailSaveRequest())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.ORDER_NOT_FOUND)));
        }

        @Test
        void isProductNotFound() throws Exception {
            doThrow(new BusinessException(ResultMessages.PRODUCT_NOT_FOUND))
                    .when(orderDetailService).add(Mockito.any(OrderDetailSaveRequest.class));

            mockMvc.perform(post("/api/order-details/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new OrderDetailSaveRequest())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.PRODUCT_NOT_FOUND)));
        }
    }

    @Nested
    class update {

        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = new GenericResponse();

            BDDMockito.given(orderDetailService.update(Mockito.any(OrderDetailUpdateRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(put("/api/order-details/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUCCESS)));

            verify(orderDetailService, times(1)).update(Mockito.any(OrderDetailUpdateRequest.class));
        }
    }

    @Nested
    class findById {
        @Test
        void isSuccess() throws Exception {
            OrderDetailId orderDetailId = new OrderDetailId(1L, 1L);
            OrderDetailDto orderDetailDto = new OrderDetailDto();

            DataGenericResponse<OrderDetailDto> mockResponse = DataGenericResponse.<OrderDetailDto>dataBuilder()
                    .data(orderDetailDto)
                    .build();

            BDDMockito.given(orderDetailService.findOrderDetailById(orderDetailId.getOrderId(), orderDetailId.getProductId()))
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/order-details/")
                            .param("orderId", String.valueOf(1L))
                            .param("productId", String.valueOf(1L))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)));

            verify(orderDetailService, Mockito.times(1)).findOrderDetailById(orderDetailId.getOrderId(), orderDetailId.getProductId());
        }
    }

    @Nested
    class delete {
        @Test
        void isSuccess() throws Exception {
            OrderDetailId orderDetailId = new OrderDetailId(1L, 1L);

            GenericResponse mockResponse = GenericResponse.builder().message(ResultMessages.RECORD_DELETED).success(true).build();

            BDDMockito.given(orderDetailService.deleteOrderDetailById(orderDetailId.getOrderId(), orderDetailId.getProductId()))
                    .willReturn(mockResponse);

            mockMvc.perform(delete("/api/order-details/")
                            .param("orderId", String.valueOf(1L))
                            .param("productId", String.valueOf(1L))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(orderDetailService, times(1)).deleteOrderDetailById(orderDetailId.getOrderId(), orderDetailId.getProductId());
        }
    }

    @Nested
    class findAll {
        @Test
        void isSuccess() throws Exception {
            OrderDetailDto dto1 = new OrderDetailDto();
            OrderDetailDto dto2 = new OrderDetailDto();


            List<OrderDetailDto> dtoList = List.of(dto1, dto2);

            DataGenericResponse<List<OrderDetailDto>> mockResponse = DataGenericResponse.<List<OrderDetailDto>>dataBuilder()
                    .data(dtoList)
                    .build();

            BDDMockito.given(orderDetailService.findAllOrderDetails())
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/order-details")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(orderDetailService, times(1)).findAllOrderDetails();
        }
    }
}