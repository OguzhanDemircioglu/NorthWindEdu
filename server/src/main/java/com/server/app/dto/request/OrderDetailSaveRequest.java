package com.server.app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailSaveRequest {
    private Long orderId;
    private Long productId;
    private Double unitPrice;
    private Long quantity;
    private Double discount;
}
