package com.server.app.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSaveRequest {
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
