package com.example.controller;


import com.example.exception.EntityNotFoundException;
import com.example.model.Product;
import com.example.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + id + " no encontrado"));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product savedProduct = productService.createProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id,@Valid @RequestBody Product product) {
        logger.info("Intentando actualizar el producto con ID: {}", id);
        Product updatedProduct = productService.updateProduct(id, product);
        logger.info("Producto actualizado correctamente: {}", updatedProduct);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Integer id) {
        productService.getProductById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + id + " no encontrado para eliminar"));
        productService.deleteProduct(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Producto eliminado exitosamente");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> partialUpdateProduct(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> updates) {

        Product product = productService.getProductById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + id + " no encontrado"));

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "name":
                        if (value instanceof String) product.setName((String) value);
                        break;
                    case "description":
                        if (value instanceof String) product.setDescription((String) value);
                        break;
                    case "price":
                        if (value instanceof Number) product.setPrice(((Number) value).intValue());
                        break;
                    case "stock":
                        if (value instanceof Number) product.setStock(((Number) value).intValue());
                        break;
                    case "pic":
                        if (value instanceof String) product.setPic((String) value);
                        break;
                    case "categoryId":
                        if (value instanceof Number) product.setCategoryId(((Number) value).intValue());
                        break;
                    case "storeId":
                        if (value instanceof Number) product.setStoreId(((Number) value).intValue());
                        break;
                    default:
                        throw new IllegalArgumentException("El campo " + key + " no es válido o no existe para actualización.");
                }
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Tipo de dato incorrecto para el campo " + key);
            }
        });

        productService.createProduct(product);
        return ResponseEntity.ok(product);
    }


}
