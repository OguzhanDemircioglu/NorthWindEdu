package com.server.app.service.srvImpl;

import com.server.app.dto.request.product.ProductSaveRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.ProductMapper;
import com.server.app.model.Category;
import com.server.app.model.Product;
import com.server.app.model.Supplier;
import com.server.app.repository.ProductRepository;
import com.server.app.service.CategoryService;
import com.server.app.service.SupplierService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductSrvImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SupplierService supplierService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductMapper productMapper;

    @InjectMocks
    private ProductSrvImpl productSrv;

    ProductSaveRequest request = new ProductSaveRequest();

    @BeforeEach
    void setUp() {
        request = new ProductSaveRequest();
        request.setProductName("Laptop");
        request.setSupplierId(1L);
        request.setCategoryId(1L);
        request.setQuantityPerUnit("10 units");
        request.setUnitPrice(1000.0);

        productMapper = new ProductMapper(productRepository, supplierService, categoryService);
        productSrv = new ProductSrvImpl(productRepository, productMapper);
    }

    @AfterEach
    void tearDown() {
        request = null;
        productMapper = null;
        productSrv = null;
    }

    @Nested
    class add {

        @Test
        void isEmptyProductStatus() {
            Supplier supplier = new Supplier();
            supplier.setSupplierId(1L);

            Category category = new Category();
            category.setCategoryId(1L);

            when(supplierService.getSupplier(1L)).thenReturn(supplier);
            when(categoryService.getCategory(1L)).thenReturn(category);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> productSrv.add(request)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.EMPTY_PRODUCT_STATUS);
        }

        @Test
        void isSupplierNotFound() {
            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> productSrv.add(request)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.SUPPLIER_NOT_FOUND);
        }

        @Test
        void isSCategoryNotFound() {
            Supplier supplier = new Supplier();
            supplier.setSupplierId(1L);

            when(supplierService.getSupplier(1L)).thenReturn(supplier);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> productSrv.add(request)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.CATEGORY_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            request.setUnitsInStock((short) 5);
            request.setUnitsInOrder((short) 2);
            request.setReorderLevel((short) 1);
            request.setDiscontinued(1);

            Supplier supplier = new Supplier();
            supplier.setSupplierId(1L);

            Category category = new Category();
            category.setCategoryId(1L);

            when(supplierService.getSupplier(1L)).thenReturn(supplier);
            when(categoryService.getCategory(1L)).thenReturn(category);
            when(productRepository.save(any(Product.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = productSrv.add(request);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.SUCCESS);
        }
    }
}