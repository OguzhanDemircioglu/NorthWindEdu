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
import com.server.app.service.CategoryService;
import com.server.app.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final ProductRepository productRepository;
    private final SupplierService supplierService;
    private final CategoryService categoryService;

    public ProductDto toDto(Product request) {
        return ProductDto.builder()
                .productId(request.getProductId())
                .productName(request.getProductName())
                .supplierId(
                        Objects.isNull(request.getSupplier())
                                ? null
                                : request.getSupplier().getSupplierId())
                .categoryId(
                        Objects.isNull(request.getCategory())
                                ? null
                                : request.getCategory().getCategoryId())
                .quantityPerUnit(request.getQuantityPerUnit())
                .unitPrice(request.getUnitPrice())
                .unitsInStock(request.getUnitsInStock())
                .unitsInOrder(request.getUnitsInOrder())
                .reorderLevel(request.getReorderLevel())
                .discontinued(request.getDiscontinued())
                .build();
    }

    public Product toEntity(ProductUpdateRequest request) {
        if (request.getProductId() == null || request.getProductId() == 0) {
            throw new BusinessException(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        boolean isExist = productRepository.existsProductByProductId(request.getProductId());
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }

        Supplier supplier = supplierService.getSupplier(request.getSupplierId());
        if (Objects.isNull(supplier)) {
            throw new BusinessException(ResultMessages.SUPPLIER_NOT_FOUND);
        }

        Category category = categoryService.getCategory(request.getCategoryId());
        if (Objects.isNull(category)) {
            throw new BusinessException(ResultMessages.CATEGORY_NOT_FOUND);
        }

        return updateEntityFromRequest(request, supplier, category);
    }

    private Product updateEntityFromRequest(ProductUpdateRequest request, Supplier supplier, Category category) {
        return Product.builder()
                .productId(request.getProductId())
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

    public Product saveEntityFromRequest(ProductSaveRequest request) {
        Supplier supplier = supplierService.getSupplier(request.getSupplierId());
        if (Objects.isNull(supplier)) {
            throw new BusinessException(ResultMessages.SUPPLIER_NOT_FOUND);
        }

        Category category = categoryService.getCategory(request.getCategoryId());
        if (Objects.isNull(category)) {
            throw new BusinessException(ResultMessages.CATEGORY_NOT_FOUND);
        }

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
