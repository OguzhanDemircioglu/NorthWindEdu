package com.server.app.service.srvImpl;

import com.google.common.base.Strings;
import com.server.app.dto.request.product.ProductSaveRequest;
import com.server.app.dto.request.product.ProductUpdateRequest;
import com.server.app.dto.response.ProductDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.mapper.ProductMapper;
import com.server.app.model.Category;
import com.server.app.model.Product;
import com.server.app.model.Supplier;
import com.server.app.repository.ProductRepository;
import com.server.app.service.CategoryService;
import com.server.app.service.ProductService;
import com.server.app.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductSrvImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SupplierService supplierService;
    private final CategoryService categoryService;
    private final ProductMapper mapper;

    @Override
    public String add(ProductSaveRequest request) {
        try {
            Supplier supplier = null;
            if(checkSupplier(request.getSupplierId())) {
                supplier = supplierService.getSupplier(request.getSupplierId());
            }
            Category category = null;
            if(checkCategory(request.getCategoryId())) {
                category = categoryService.getCategory(request.getCategoryId());
            }
            Product product = mapper.saveEntityFromRequest(request, supplier, category);


            BusinessRules.validate(
                    checkProductForGeneralValidations(product),
                    checkNameValidation(product.getProductName()),
                    checkQuantityValidation(product.getQuantityPerUnit())
            );

            productRepository.save(product);
        } catch (BusinessException e) {
            log.error("Business validation failed for product add: {}", request.getProductName(), e);
            throw e;
        } catch (Exception e) {
            return ResultMessages.PROCESS_FAILED;
        }
        return ResultMessages.SUCCESS;
    }

    @Override
    public ProductDto update(ProductUpdateRequest request) {
        try {
            Supplier supplier = null;
            if(checkSupplier(request.getSupplierId())) {
                supplier = supplierService.getSupplier(request.getSupplierId());
            }
            Category category = null;
            if(checkCategory(request.getCategoryId())) {
                category = categoryService.getCategory(request.getCategoryId());
            }
            Product product = mapper.toEntity(request, supplier, category);

            BusinessRules.validate(
                    checkProductForGeneralValidations(product),
                    checkNameValidation(product.getProductName()),
                    checkQuantityValidation(product.getQuantityPerUnit())
            );

            Product updatedProduct = productRepository.save(product);

            return mapper.toDto(updatedProduct);
        }catch (BusinessException e) {
            log.error("Business validation failed for product update: {}", request.getProductId() +" "+request.getProductName(), e);
            throw e;
        }catch (Exception e) {
            log.error("Product update failed for ID: {}", request.getProductId(), e);
            throw new BusinessException(ResultMessages.PROCESS_FAILED + ": " + e.getMessage());
        }
    }

    @Override
    public ProductDto findProductByProductId(Short productId) {
        Optional<Product> product = productRepository.findProductByProductId(productId);
        if (product.isEmpty()) {
            throw new RuntimeException(ResultMessages.RECORD_NOT_FOUND);
        }
        return mapper.toDto(product.get());
    }

    @Override
    public void deleteProductByProductId(Short productId) { productRepository.deleteProductByProductId(productId); }

    @Override
    public List<ProductDto> findAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> result = new ArrayList<>();

        for (Product p : products) {
            ProductDto dto = mapper.toDto(p);
            result.add(dto);
        }
        return result;
    }

    @Override
    public boolean checkCategory(Short categoryId) {
        if(!categoryService.existsCategoryById(categoryId)) {
            log.warn(ResultMessages.CATEGORY_NOT_FOUND);
            return false;
        }
        return true;
    }

    @Override
    public boolean checkSupplier(Short supplierId) {
        if(!supplierService.existsSupplierById(supplierId)) {
            log.warn(ResultMessages.SUPPLIER_NOT_FOUND);
            return false;
        }
        return true;
    }

    private String checkNameValidation(String name) {
        if(!Strings.isNullOrEmpty(name) && name.length() > 40) {
            return ResultMessages.P_NAME_OUT_OF_RANGE;
        }
        return null;
    }

    private String checkQuantityValidation(String quantity) {
        if(quantity.length() > 20) {
            return ResultMessages.QUANTITY_OUT_OF_RANGE;
        }
        return null;
    }

    private String checkProductForGeneralValidations(Product request) {

        if(Strings.isNullOrEmpty(request.getProductName())) {
            return ResultMessages.EMPTY_NAME;
        }

        if(request.getDiscontinued() == null) {
            return ResultMessages.EMPTY_PRODUCT_STATUS;
        }
        return null;
    }


}
