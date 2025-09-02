package com.server.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.dto.request.order.OrderSaveRequest;
import com.server.app.dto.request.order.OrderUpdateRequest;
import com.server.app.dto.request.territory.TerritorySaveRequest;
import com.server.app.dto.response.OrderDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.JWTService;
import com.server.app.service.OrderService;
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

@WebMvcTest(controllers = OrderController.class, excludeAutoConfiguration =  SecurityAutoConfiguration.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UserService userService;

    OrderSaveRequest saveRequest = new OrderSaveRequest();
    OrderUpdateRequest updateRequest = new OrderUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setCustomerId("cus1");
        saveRequest.setEmployeeId(1L);

        updateRequest.setCustomerId("cus2");
        updateRequest.setEmployeeId(2L);
    }

    @AfterEach
    void tearDown() {
        saveRequest = null;
        updateRequest = null;
        orderService = null;
        userService = null;
        jwtService = null;
        mockMvc = null;
        objectMapper = null;
    }

    @Nested
    class add {

        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = new GenericResponse();

            BDDMockito.given(orderService.add(Mockito.any(OrderSaveRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(post("/api/orders/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUCCESS)));

            verify(orderService, times(1)).add(Mockito.any(OrderSaveRequest.class));
        }

        @Test
        void notValidRequest() throws Exception {
            doThrow(new BusinessException(ResultMessages.ORDER_NOT_FOUND))
                    .when(orderService).add(Mockito.any(OrderSaveRequest.class));

            mockMvc.perform(post("/api/orders/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new TerritorySaveRequest())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.ORDER_NOT_FOUND)));
        }

        @Test
        void isEmptyId() throws Exception {
            doThrow(new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED))
                    .when(orderService).add(Mockito.any(OrderSaveRequest.class));

            mockMvc.perform(post("/api/orders/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.ID_IS_NOT_DELIVERED)));
        }

        @Test
        void isEmployeeNotFound() throws Exception {
            doThrow(new BusinessException(ResultMessages.EMPLOYEE_NOT_FOUND))
                    .when(orderService).add(Mockito.any(OrderSaveRequest.class));

            mockMvc.perform(post("/api/orders/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.EMPLOYEE_NOT_FOUND)));
        }

        @Test
        void isCustomerNotFound() throws Exception {
            doThrow(new BusinessException(ResultMessages.CUSTOMER_NOT_FOUND))
                    .when(orderService).add(Mockito.any(OrderSaveRequest.class));

            mockMvc.perform(post("/api/orders/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.CUSTOMER_NOT_FOUND)));
        }

        @Test
        void isShipperNotFound() throws Exception {
            doThrow(new BusinessException(ResultMessages.SHIPPER_NOT_FOUND))
                    .when(orderService).add(Mockito.any(OrderSaveRequest.class));

            mockMvc.perform(post("/api/orders/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SHIPPER_NOT_FOUND)));
        }

        @Test
        void isInvalidShipName() throws Exception {
            doThrow(new BusinessException(ResultMessages.SHIP_NAME_OUT_OF_RANGE))
                    .when(orderService).add(Mockito.any(OrderSaveRequest.class));

            mockMvc.perform(post("/api/orders/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SHIP_NAME_OUT_OF_RANGE)));
        }

        @Test
        void isInvalidShipAddress() throws Exception {
            doThrow(new BusinessException(ResultMessages.SHIP_ADDRESS_OUT_OF_RANGE))
                    .when(orderService).add(Mockito.any(OrderSaveRequest.class));

            mockMvc.perform(post("/api/orders/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SHIP_ADDRESS_OUT_OF_RANGE)));
        }

        @Test
        void isInvalidShipCity() throws Exception {
            doThrow(new BusinessException(ResultMessages.SHIP_CITY_OUT_OF_RANGE))
                    .when(orderService).add(Mockito.any(OrderSaveRequest.class));

            mockMvc.perform(post("/api/orders/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SHIP_CITY_OUT_OF_RANGE)));
        }

        @Test
        void isInvalidShipCountry() throws Exception {
            doThrow(new BusinessException(ResultMessages.SHIP_COUNTRY_OUT_OF_RANGE))
                    .when(orderService).add(Mockito.any(OrderSaveRequest.class));

            mockMvc.perform(post("/api/orders/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SHIP_COUNTRY_OUT_OF_RANGE)));
        }

        @Test
        void isInvalidShipRegion() throws Exception {
            doThrow(new BusinessException(ResultMessages.SHIP_REGION_OUT_OF_RANGE))
                    .when(orderService).add(Mockito.any(OrderSaveRequest.class));

            mockMvc.perform(post("/api/orders/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SHIP_REGION_OUT_OF_RANGE)));
        }

        @Test
        void isInvalidShipPostalCode() throws Exception {
            doThrow(new BusinessException(ResultMessages.SHIP_POSTAL_CODE_OUT_OF_RANGE))
                    .when(orderService).add(Mockito.any(OrderSaveRequest.class));

            mockMvc.perform(post("/api/orders/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(false)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SHIP_POSTAL_CODE_OUT_OF_RANGE)));
        }
    }

    @Nested
    class update {

        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = new GenericResponse();

            BDDMockito.given(orderService.update(Mockito.any(OrderUpdateRequest.class)))
                    .willReturn(mockResponse);

            mockMvc.perform(put("/api/orders/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                    .andExpect(jsonPath("$.message", CoreMatchers.is(ResultMessages.SUCCESS)));

            verify(orderService, times(1)).update(Mockito.any(OrderUpdateRequest.class));
        }
    }

    @Nested
    class findById {
        @Test
        void isSuccess() throws Exception {
            OrderDto orderDto = new OrderDto();

            DataGenericResponse<OrderDto> mockResponse = DataGenericResponse.<OrderDto>dataBuilder()
                    .data(orderDto)
                    .build();

            BDDMockito.given(orderService.findOrderByOrderId(1L))
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/orders/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", CoreMatchers.is(true)));

            verify(orderService, Mockito.times(1)).findOrderByOrderId(1L);
        }
    }

    @Nested
    class delete {
        @Test
        void isSuccess() throws Exception {
            GenericResponse mockResponse = GenericResponse.builder().message(ResultMessages.RECORD_DELETED).success(true).build();

            BDDMockito.given(orderService.deleteOrderByOrderId(1L))
                    .willReturn(mockResponse);

            mockMvc.perform(delete("/api/orders/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(orderService, times(1)).deleteOrderByOrderId(1L);
        }
    }

    @Nested
    class findAll {
        @Test
        void isSuccess() throws Exception {
            OrderDto dto1 = new OrderDto();
            OrderDto dto2 = new OrderDto();

            List<OrderDto> orderDtoList = List.of(dto1, dto2);

            DataGenericResponse<List<OrderDto>> mockResponse = DataGenericResponse.<List<OrderDto>>dataBuilder()
                    .data(orderDtoList)
                    .build();

            BDDMockito.given(orderService.findAllOrders())
                    .willReturn(mockResponse);

            mockMvc.perform(get("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(orderService, times(1)).findAllOrders();
        }
    }

}