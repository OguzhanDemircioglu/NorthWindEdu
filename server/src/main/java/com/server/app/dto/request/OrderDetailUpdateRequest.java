package com.server.app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailUpdateRequest {
    private Long orderId;
    private Long productId;
    private Float unitPrice;
    private Long quantity;
    private Float discount;
}
