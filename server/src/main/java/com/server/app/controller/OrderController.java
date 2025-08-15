package com.server.app.controller;

import com.server.app.dto.OrderDto;
import com.server.app.dto.request.OrderSaveRequest;
import com.server.app.dto.request.OrderUpdateRequest;
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
    public ResponseEntity<?> add(@RequestBody OrderSaveRequest request){
        String resultMessage = orderService.add(request);
        return ResponseEntity.ok(resultMessage);
    }

    @PutMapping("/update")
    public ResponseEntity<OrderDto> update(@RequestBody OrderUpdateRequest request){
        OrderDto updatedOrder = orderService.update(request);
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findOrderByOrderId(@PathVariable Short id){
        OrderDto order = orderService.findOrderByOrderId(id);
        return ResponseEntity.ok(order);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Short id){
        orderService.deleteOrderByOrderId(id);
        return ResponseEntity.ok("İşlem Başarılı");
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> findAllOrders(){
        return ResponseEntity.ok(orderService.findAllOrders());
    }
}
