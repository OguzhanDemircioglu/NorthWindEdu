package com.server.app.controller;

import com.server.app.dto.response.OrderDto;
import com.server.app.dto.request.order.OrderSaveRequest;
import com.server.app.dto.request.order.OrderUpdateRequest;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<GenericResponse> add(@RequestBody OrderSaveRequest request) {
        return ResponseEntity.ok(orderService.add(request));
    }

    @PutMapping("/update")
    public ResponseEntity<GenericResponse> update(@RequestBody OrderUpdateRequest request) {
        return ResponseEntity.ok(orderService.update(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataGenericResponse<OrderDto>> get(@PathVariable Long id) {
        DataGenericResponse<OrderDto> result = orderService.findOrderByOrderId(id);
        return ResponseEntity.ok(result);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> delete(@PathVariable Long id) {
        GenericResponse result = orderService.deleteOrderByOrderId(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<DataGenericResponse<List<OrderDto>>> findAllOrders() {
        DataGenericResponse<List<OrderDto>> result = orderService.findAllOrders();
        return ResponseEntity.ok(result);
    }
}
