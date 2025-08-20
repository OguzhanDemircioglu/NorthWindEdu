package com.server.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {
    private Long orderId;
    private Long productId;
    private Double unitPrice;
    private Long quantity;   // smallint -> Long (senin istediğin)
    private Double discount;
}
