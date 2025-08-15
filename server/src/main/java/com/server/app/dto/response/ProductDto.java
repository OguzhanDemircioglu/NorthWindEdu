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
    private Short productId;
    private String productName;
    private Short supplierId;
    private Short categoryId;
    private String quantityPerUnit;
    private Float unitPrice;
    private Integer discontinued;
    private Short reorderLevel;
    private Short unitsInStock;
    private Short unitsInOrder;
}
