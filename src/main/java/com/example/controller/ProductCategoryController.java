package com.example.controller;

import com.example.dto.ProductCategoryDto;
import com.example.model.ProductCategory;
import com.example.service.ProductCategoryService;
import com.example.utility.ProductCategoryMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product-categories")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;
    private final ProductCategoryMapper productCategoryMapper;

    public ProductCategoryController(ProductCategoryService productCategoryService, ProductCategoryMapper productCategoryMapper) {
        this.productCategoryService = productCategoryService;
        this.productCategoryMapper = productCategoryMapper;
    }

    @GetMapping
    public ResponseEntity<List<ProductCategoryDto>> getAllCategories() {
        return new ResponseEntity<>(
                productCategoryService.getAllProductCategories().stream().map(productCategoryMapper::toPublicDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryDto> getCategoryById(@PathVariable Integer id) {
        return new ResponseEntity<>(
                productCategoryMapper.toPublicDto(productCategoryService.getProductCategoryById(id)),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createCategory(@Valid @RequestBody ProductCategoryDto categoryDto) {
        ProductCategory created = productCategoryService.createProductCategory(categoryDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Categoría: " + created.getName() + " con ID: " + created.getId() + ", creada exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Integer id) {
        String name = productCategoryService.getProductCategoryById(id).getName();
        productCategoryService.deleteProductCategory(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Categoría: " + name + " de ID: " + id + ", fue eliminada exitosamente");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateCategory(@PathVariable Integer id, @Valid @RequestBody ProductCategoryDto categoryDto) {
        ProductCategory updated = productCategoryService.updateProductCategory(id, categoryDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Categoría: " + updated.getName() + " de ID: " + id + ", actualizada exitosamente");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> partialUpdateCategory(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        ProductCategory updated = productCategoryService.partialUpdateProductCategory(id, updates);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Categoría: " + updated.getName() + ", campo/s actualizado/s exitosamente");
        return ResponseEntity.ok(response);
    }
}
