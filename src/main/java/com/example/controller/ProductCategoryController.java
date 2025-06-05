package com.example.controller;

import com.example.dto.ProductCategoryDto;
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
    public ResponseEntity<List<ProductCategoryDto>> getAllCategories() {
        List<ProductCategory> categories = productCategoryService.getAllProductCategories();
        List<ProductCategoryDto> dtos = new ArrayList<>();
        for (ProductCategory category : categories) {
            ProductCategoryDto dto = new ProductCategoryDto();
            dto.setName(category.getName());
            dto.setDescription(category.getDescription());
            dto.setStoreId(category.getStoreId());
            dtos.add(dto);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryDto> getCategoryById(@PathVariable Integer id) {
        ProductCategory category = productCategoryService.getProductCategoryById(id);
        ProductCategoryDto dto = new ProductCategoryDto();
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setStoreId(category.getStoreId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createCategory(@Valid @RequestBody ProductCategory category) {
        ProductCategory created = productCategoryService.createProductCategory(category);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Categoría '" + created.getName() + "' creada exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Integer id) {
        String name = productCategoryService.getProductCategoryById(id).getName();
        productCategoryService.deleteProductCategory(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Categoría '" + name + "' eliminada exitosamente");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateCategory(@PathVariable Integer id, @Valid @RequestBody ProductCategory category) {
        ProductCategory updated = productCategoryService.updateProductCategory(id, category);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Categoría '" + updated.getName() + "' actualizada exitosamente");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> partialUpdateCategory(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        ProductCategory updated = productCategoryService.partialUpdateProductCategory(id, updates);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Categoría '" + updated.getName() + "' actualizada parcialmente");
        return ResponseEntity.ok(response);
    }
}
