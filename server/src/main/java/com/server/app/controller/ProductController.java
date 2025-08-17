package com.server.app.controller;

import com.server.app.dto.request.product.ProductSaveRequest;
import com.server.app.dto.request.product.ProductUpdateRequest;
import com.server.app.dto.response.ProductDto;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<GenericResponse> add(@RequestBody ProductSaveRequest request) {
        return ResponseEntity.ok(productService.add(request));
    }

    @PutMapping("/update")
    public ResponseEntity<DataGenericResponse<ProductDto>> update(@RequestBody ProductUpdateRequest request) {
        return ResponseEntity.ok(productService.update(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataGenericResponse<ProductDto>> get(@PathVariable Long id) {
        DataGenericResponse<ProductDto> result = productService.findProductByProductId(id);
        return ResponseEntity.ok(result);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> delete(@PathVariable Long id) {
        GenericResponse result = productService.deleteProductByProductId(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<DataGenericResponse<List<ProductDto>>> findAllProducts() {
        DataGenericResponse<List<ProductDto>> result = productService.findAllProducts();
        return ResponseEntity.ok(result);
    }
}
