package com.server.app.service;

import com.server.app.dto.request.product.ProductSaveRequest;
import com.server.app.dto.request.product.ProductUpdateRequest;
import com.server.app.dto.response.ProductDto;

import java.util.List;

public interface ProductService {

    String add(ProductSaveRequest request);

    ProductDto update(ProductUpdateRequest request);

    ProductDto findProductByProductId(Short productId);

    void deleteProductByProductId(Short productId);

    List<ProductDto> findAllProducts();
}
