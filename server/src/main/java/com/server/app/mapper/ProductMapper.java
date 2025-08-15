package com.server.app.mapper;

import com.server.app.dto.request.product.ProductSaveRequest;
import com.server.app.dto.request.product.ProductUpdateRequest;
import com.server.app.dto.response.ProductDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.model.Product;
import com.server.app.repository.CategoryRepository;
import com.server.app.repository.ProductRepository;
import com.server.app.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;

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

    public Product toEntity(ProductUpdateRequest request) {
        Product existingProduct = productRepository.findProductByProductId(request.getProductId())
                .orElseThrow(() -> new BusinessException(ResultMessages.RECORD_NOT_FOUND));

        return updateEntityFromRequest(request, existingProduct);
    }

    private Product updateEntityFromRequest(ProductUpdateRequest request, Product existingProduct) {
        return Product.builder()
                .productId(existingProduct.getProductId())
                .productName(request.getProductName())
                .supplier(supplierRepository.findSupplierBySupplierId(request.getSupplierId())
                        .orElse(null))
                .category(categoryRepository.findCategoryByCategoryId(request.getCategoryId())
                        .orElse(null))
                .quantityPerUnit(request.getQuantityPerUnit())
                .unitPrice(request.getUnitPrice())
                .unitsInOrder(request.getUnitsInOrder())
                .unitsInStock(request.getUnitsInStock())
                .reorderLevel(request.getReorderLevel())
                .discontinued(request.getDiscontinued())
                .build();
    }

    public Product saveEntityFromRequest(ProductSaveRequest request) {
        return Product.builder()
                .productName(request.getProductName())
                .supplier(supplierRepository.findSupplierBySupplierId(request.getSupplierId())
                        .orElse(null))
                .category(categoryRepository.findCategoryByCategoryId(request.getCategoryId())
                        .orElse(null))
                .quantityPerUnit(request.getQuantityPerUnit())
                .unitPrice(request.getUnitPrice())
                .unitsInStock(request.getUnitsInStock())
                .unitsInOrder(request.getUnitsInOrder())
                .reorderLevel(request.getReorderLevel())
                .discontinued(request.getDiscontinued())
                .build();
    }
}
