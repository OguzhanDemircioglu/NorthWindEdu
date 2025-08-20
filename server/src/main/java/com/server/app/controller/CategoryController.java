package com.server.app.controller;

import com.server.app.dto.response.CategoryDto;
import com.server.app.dto.request.category.CategorySaveRequest;
import com.server.app.dto.request.category.CategoryUpdateRequest;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
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
    public ResponseEntity<GenericResponse> add(@RequestBody CategorySaveRequest request) {
        return ResponseEntity.ok(categoryService.add(request));
    }

    @PutMapping("/update")
    public ResponseEntity<GenericResponse> update(@RequestBody CategoryUpdateRequest request) {
        return ResponseEntity.ok(categoryService.update(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataGenericResponse<CategoryDto>> findCategoryById(@PathVariable Long id) {
        DataGenericResponse<CategoryDto> result = categoryService.findCategoryByCategoryId(id);
        return ResponseEntity.ok(result);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteCategoryById(@PathVariable Long id) {
        GenericResponse result = categoryService.deleteCategoryByCategoryId(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<DataGenericResponse<List<CategoryDto>>> findAllEmployees(){
        DataGenericResponse<List<CategoryDto>> result = categoryService.findAllCategories();
        return ResponseEntity.ok(result);
    }
}
