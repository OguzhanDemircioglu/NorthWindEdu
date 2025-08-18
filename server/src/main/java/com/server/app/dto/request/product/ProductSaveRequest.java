package com.server.app.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSaveRequest {
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
