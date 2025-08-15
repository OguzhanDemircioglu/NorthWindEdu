package com.server.app.mapper;

import com.server.app.dto.request.product.ProductSaveRequest;
import com.server.app.dto.request.product.ProductUpdateRequest;
import com.server.app.dto.response.ProductDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.model.Category;
import com.server.app.model.Product;
import com.server.app.model.Supplier;
import com.server.app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final ProductRepository productRepository;

    public ProductDto toDto(Product request) {
        return ProductDto.builder()
                .productId(request.getProductId())
                .productName(request.getProductName())
                .supplierId(request.getSupplier() !=null ? request.getSupplier().getSupplierId() : null)
                .categoryId(request.getCategory() !=null ? request.getCategory().getCategoryId() : null)
                .quantityPerUnit(request.getQuantityPerUnit())
                .unitPrice(request.getUnitPrice())
                .unitsInStock(request.getUnitsInStock())
                .unitsInOrder(request.getUnitsInOrder())
                .reorderLevel(request.getReorderLevel())
                .discontinued(request.getDiscontinued())
                .build();
    }

    public Product toEntity(ProductUpdateRequest request, Supplier supplier, Category category) {
        Product existingProduct = productRepository.findProductByProductId(request.getProductId())
                .orElseThrow(() -> new BusinessException(ResultMessages.RECORD_NOT_FOUND));

        return updateEntityFromRequest(request, existingProduct, supplier, category);
    }

    private Product updateEntityFromRequest(ProductUpdateRequest request, Product existingProduct, Supplier supplier,  Category category) {
        return Product.builder()
                .productId(existingProduct.getProductId())
                .productName(request.getProductName())
                .supplier(supplier)
                .category(category)
                .quantityPerUnit(request.getQuantityPerUnit())
                .unitPrice(request.getUnitPrice())
                .unitsInOrder(request.getUnitsInOrder())
                .unitsInStock(request.getUnitsInStock())
                .reorderLevel(request.getReorderLevel())
                .discontinued(request.getDiscontinued())
                .build();
    }

    public Product saveEntityFromRequest(ProductSaveRequest request, Supplier supplier, Category category) {
        return Product.builder()
                .productName(request.getProductName())
                .supplier(supplier)
                .category(category)
                .quantityPerUnit(request.getQuantityPerUnit())
                .unitPrice(request.getUnitPrice())
                .unitsInStock(request.getUnitsInStock())
                .unitsInOrder(request.getUnitsInOrder())
                .reorderLevel(request.getReorderLevel())
                .discontinued(request.getDiscontinued())
                .build();
    }
}
