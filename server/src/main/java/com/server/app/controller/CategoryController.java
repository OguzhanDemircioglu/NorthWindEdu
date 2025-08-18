package com.server.app.controller;

import com.server.app.dto.CategoryDto;
import com.server.app.dto.request.CategorySaveRequest;
import com.server.app.dto.request.CategoryUpdateRequest;
import com.server.app.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody CategorySaveRequest request) {
        String resultMessage = categoryService.add(request);
        return ResponseEntity.ok(resultMessage);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody CategoryUpdateRequest request) {
        CategoryDto updatedCategory = categoryService.update(request);
        return ResponseEntity.ok(updatedCategory);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.findCategoryByCategoryId(id);
        return ResponseEntity.ok(category);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryByCategoryId(id);
        return ResponseEntity.ok("İşlem Başarılı");
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAllEmployees(){
        return ResponseEntity.ok(categoryService.findAllCategories());
    }
}
