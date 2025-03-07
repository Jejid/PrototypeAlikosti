package com.example.controller;

import com.example.model.Product;
import com.example.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

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
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        System.out.println("Intentando actualizar el producto con ID: " + id);
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            System.out.println("Producto actualizado correctamente: " + updatedProduct);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> partialUpdateProduct(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> updates) {

        return productService.getProductById(id).map(product -> {

            updates.forEach((key, value) -> {
                switch (key) {
                    case "name": product.setName((String) value); break;
                    case "description": product.setDescription((String) value); break;
                    case "price": product.setPrice((Integer) value); break;
                    case "stock": product.setStock((Integer) value); break;
                    case "pic": product.setPic((String) value); break;
                    case "categoryId": product.setCategoryId((Integer) value); break;
                    case "storeId": product.setStoreId((Integer) value); break;
                }
            });

            productService.createProduct(product);
            return ResponseEntity.ok(product);
        }).orElse(ResponseEntity.notFound().build());
    }

}
