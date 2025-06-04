package com.example.controller;

import com.example.model.ProductCategory;
import com.example.service.ProductCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/product-categories")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<ProductCategory>> getAllProductCategories() {
        return new ResponseEntity<>(productCategoryService.getAllProductCategories(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategory> getProductCategoryById(@PathVariable Integer id) {
        return new ResponseEntity<>(productCategoryService.getProductCategoryById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createProductCategory(@Valid @RequestBody ProductCategory category) {
        ProductCategory created = productCategoryService.createProductCategory(category);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Categoría: " + created.getName() + " creada exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateProductCategory(
            @PathVariable Integer id,
            @Valid @RequestBody ProductCategory category
    ) {
        ProductCategory updated = productCategoryService.updateProductCategory(id, category);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Categoría: " + updated.getName() + " actualizada exitosamente");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProductCategory(@PathVariable Integer id) {
        String name = productCategoryService.getProductCategoryById(id).getName();
        productCategoryService.deleteProductCategory(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Categoría: " + name + " eliminada exitosamente");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> partialUpdateProductCategory(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> updates
    ) {
        ProductCategory updated = productCategoryService.partialUpdateProductCategory(id, updates);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Categoría: " + updated.getName() + " actualizada parcialmente con éxito");
        return ResponseEntity.ok(response);
    }
}
