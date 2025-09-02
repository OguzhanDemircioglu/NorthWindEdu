package com.server.app.service;

import com.server.app.dto.request.product.ProductSaveRequest;
import com.server.app.dto.request.product.ProductUpdateRequest;
import com.server.app.dto.response.ProductDto;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.model.Product;

import java.util.List;

public interface ProductService {

    GenericResponse add(ProductSaveRequest request);

    GenericResponse update(ProductUpdateRequest request);

    DataGenericResponse<ProductDto> findProductByProductId(Long productId);

    GenericResponse deleteProductByProductId(Long productId);

    DataGenericResponse<List<ProductDto>> findAllProducts();

    Product getProduct(Long id);
}
