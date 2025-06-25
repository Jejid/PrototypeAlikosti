package com.example.controller;

import com.example.dto.ProductDto;
import com.example.model.Product;
import com.example.service.ProductService;
import com.example.utility.ProductMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return new ResponseEntity<>(
                productService.getAllProducts().stream()
                        .map(productMapper::toPublicDto)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id) {
        return new ResponseEntity<>(
                productMapper.toPublicDto(productService.getProductById(id)),
                HttpStatus.OK);
    }

    @GetMapping("store/{storeId}/category/{categoryId}")
    public ResponseEntity<List<ProductDto>> getProductsByCategoryStore(@PathVariable int storeId, @PathVariable int categoryId) {
        return new ResponseEntity<>(productService.getProductByCategoryStore(storeId, categoryId).stream().map(productMapper::toPublicDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createProduct(@Valid @RequestBody ProductDto productDto) {
        Product created = productService.createProduct(productDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Producto: " + created.getName() + " con ID: " + created.getId() + ", creado exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Integer id) {
        String name = productService.getProductById(id).getName();
        productService.deleteProduct(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Producto: " + name + " de ID: " + id + ", fue eliminado exitosamente");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateProduct(@PathVariable Integer id, @Valid @RequestBody ProductDto productDto) {
        Product updated = productService.updateProduct(id, productDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Producto: " + updated.getName() + " de ID: " + id + ", actualizado exitosamente");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> partialUpdateProduct(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        Product updated = productService.partialUpdateProduct(id, updates);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Producto: " + updated.getName() + ", campo/s actualizado/s exitosamente");
        return ResponseEntity.ok(response);
    }
}
