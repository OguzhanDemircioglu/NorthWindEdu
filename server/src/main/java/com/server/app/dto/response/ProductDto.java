package com.server.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long productId;
    private String productName;
    private Long supplierId;
    private Long categoryId;
    private String quantityPerUnit;
    private Double unitPrice;
    private Integer discontinued;
    private Short reorderLevel;
    private Short unitsInStock;
    private Short unitsInOrder;
}
