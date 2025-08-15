package com.server.app.controller;

import com.server.app.dto.request.product.ProductSaveRequest;
import com.server.app.dto.request.product.ProductUpdateRequest;
import com.server.app.dto.response.ProductDto;
import com.server.app.enums.ResultMessages;
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
    public ResponseEntity<?> add(@RequestBody ProductSaveRequest request) {
        String resultMessage = productService.add(request);
        return ResponseEntity.ok(resultMessage);
    }

    @PutMapping("/update")
    public ResponseEntity<ProductDto> update(@RequestBody ProductUpdateRequest request) {
        ProductDto updatedProduct = productService.update(request);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> get(@PathVariable Short id) {
        ProductDto product = productService.findProductByProductId(id);
        return ResponseEntity.ok(product);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Short id) {
        productService.deleteProductByProductId(id);
        return ResponseEntity.ok(ResultMessages.SUCCESS);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> findAllProducts() {
        return  ResponseEntity.ok(productService.findAllProducts());
    }
}
