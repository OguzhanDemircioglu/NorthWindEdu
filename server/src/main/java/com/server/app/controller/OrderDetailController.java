package com.server.app.controller;

import com.server.app.dto.request.OrderDetailSaveRequest;
import com.server.app.dto.request.OrderDetailUpdateRequest;
import com.server.app.dto.response.OrderDetailDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.service.OrderDetailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-details")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody OrderDetailSaveRequest request){
        orderDetailService.add(request);
        return ResponseEntity.ok(ResultMessages.SUCCESS);
    }

    @PutMapping("/update")
    public ResponseEntity<OrderDetailDto> update(@RequestBody OrderDetailUpdateRequest request){
        orderDetailService.update(request);
        DataGenericResponse<OrderDetailDto> latest =
                orderDetailService.findOrderDetailById(request.getOrderId(), request.getProductId());
        return ResponseEntity.ok(latest.getData());
    }

    @GetMapping("/")
    public ResponseEntity<OrderDetailDto> findById(@RequestParam Long orderId,
                                                   @RequestParam Long productId){
        DataGenericResponse<OrderDetailDto> dto = orderDetailService.findOrderDetailById(orderId, productId);
        return ResponseEntity.ok(dto.getData());
    }

    @Transactional
    @DeleteMapping("/")
    public ResponseEntity<String> delete(@RequestParam Long orderId,
                                         @RequestParam Long productId){
        orderDetailService.deleteOrderDetailById(orderId, productId);
        return ResponseEntity.ok(ResultMessages.RECORD_DELETED);
    }

    @GetMapping
    public ResponseEntity<List<OrderDetailDto>> findAll(){
        DataGenericResponse<List<OrderDetailDto>> all = orderDetailService.findAllOrderDetails();
        return ResponseEntity.ok(all.getData());
    }
}
