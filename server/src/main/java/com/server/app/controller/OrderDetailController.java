package com.server.app.controller;

import com.server.app.dto.request.orderDetail.OrderDetailSaveRequest;
import com.server.app.dto.request.orderDetail.OrderDetailUpdateRequest;
import com.server.app.dto.response.OrderDetailDto;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.OrderDetailService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-details")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;
    public OrderDetailController(@Lazy OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @PostMapping("/add")
    public ResponseEntity<GenericResponse> add(@RequestBody OrderDetailSaveRequest request) {
        return ResponseEntity.ok(orderDetailService.add(request));
    }

    @PutMapping("/update")
    public ResponseEntity<GenericResponse> update(@RequestBody OrderDetailUpdateRequest request) {
        return ResponseEntity.ok(orderDetailService.update(request));
    }

    @GetMapping("/")
    public ResponseEntity<DataGenericResponse<OrderDetailDto>> get(@RequestParam Long orderId,
                                                                   @RequestParam Long productId) {
        DataGenericResponse<OrderDetailDto> result = orderDetailService.findOrderDetailById(orderId, productId);
        return ResponseEntity.ok(result);
    }

    @Transactional
    @DeleteMapping("/")
    public ResponseEntity<GenericResponse> delete(@RequestParam Long orderId,
                                                  @RequestParam Long productId) {
        GenericResponse result = orderDetailService.deleteOrderDetailById(orderId, productId);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<DataGenericResponse<List<OrderDetailDto>>> findAllOrderDetails() {
        DataGenericResponse<List<OrderDetailDto>> result = orderDetailService.findAllOrderDetails();
        return ResponseEntity.ok(result);
    }
}
