package com.server.app.service.srvImpl;

import com.google.common.base.Strings;
import com.server.app.dto.request.product.ProductSaveRequest;
import com.server.app.dto.request.product.ProductUpdateRequest;
import com.server.app.dto.response.ProductDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.ProductMapper;
import com.server.app.model.Product;
import com.server.app.repository.ProductRepository;
import com.server.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductSrvImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    @Override
    public GenericResponse add(ProductSaveRequest request) {
        Product product = mapper.saveEntityFromRequest(request);

        BusinessRules.validate(
                checkProductForGeneralValidations(product),
                checkNameValidation(product.getProductName()),
                checkQuantityValidation(product.getQuantityPerUnit())
        );

        productRepository.save(product);
        return new GenericResponse();
    }

    @Override
    public GenericResponse update(ProductUpdateRequest request) {
        Product product = mapper.toEntity(request);

        BusinessRules.validate(
                checkProductForGeneralValidations(product),
                checkNameValidation(product.getProductName()),
                checkQuantityValidation(product.getQuantityPerUnit())
        );

        productRepository.save(product);
        return GenericResponse.builder().message(ResultMessages.RECORD_UPDATED).build();
    }

    @Override
    public DataGenericResponse<ProductDto> findProductByProductId(Long productId) {
        Optional<Product> product = productRepository.findProductByProductId(productId);
        if (product.isEmpty()) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }
        ProductDto dto = mapper.toDto(product.get());
        return DataGenericResponse.<ProductDto>dataBuilder()
                .data(dto)
                .build();
    }

    @Override
    public GenericResponse deleteProductByProductId(Long productId) {
        boolean isExist = productRepository.existsProductByProductId(productId);
        if (!isExist) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }
        productRepository.deleteProductByProductId(productId);

        return GenericResponse.builder().message(ResultMessages.RECORD_DELETED).build();
    }

    @Override
    public DataGenericResponse<List<ProductDto>> findAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> dtos = products.stream()
                .map(mapper::toDto)
                .toList();

        return DataGenericResponse.<List<ProductDto>>dataBuilder()
                .data(dtos)
                .build();
    }

    private String checkNameValidation(String name) {
        if (!Strings.isNullOrEmpty(name) && name.length() > 40) {
            return ResultMessages.P_NAME_OUT_OF_RANGE;
        }
        return null;
    }

    private String checkQuantityValidation(String quantity) {
        if (quantity.length() > 20) {
            return ResultMessages.QUANTITY_OUT_OF_RANGE;
        }
        return null;
    }

    private String checkProductForGeneralValidations(Product request) {

        if (Strings.isNullOrEmpty(request.getProductName())) {
            return ResultMessages.EMPTY_NAME;
        }

        if (request.getDiscontinued() == null) {
            return ResultMessages.EMPTY_PRODUCT_STATUS;
        }
        return null;
    }
}