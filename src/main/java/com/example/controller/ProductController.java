package com.example.controller;

import com.example.exception.ProductNotFoundException;
import com.example.model.Product;
import com.example.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                .orElseThrow(() -> new ProductNotFoundException("Producto con ID " + id + " no encontrado"));
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        logger.info("Intentando actualizar el producto con ID: {}", id);
        Product updatedProduct = productService.updateProduct(id, product);
        logger.info("Producto actualizado correctamente: {}", updatedProduct);
        return ResponseEntity.ok(updatedProduct);
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

        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto con ID " + id + " no encontrado"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name": product.setName((String) value); break;
                case "description": product.setDescription((String) value); break;
                case "price": product.setPrice((Integer) value); break;
                case "stock": product.setStock((Integer) value); break;
                case "pic": product.setPic((String) value); break;
                case "categoryId": product.setCategoryId((Integer) value); break;
                case "storeId": product.setStoreId((Integer) value); break;
                default:
                    throw new IllegalArgumentException("El campo " + key + " no existe en la tabla o no es válido para actualización");
            }
        });

        productService.createProduct(product);
        return ResponseEntity.ok(product);
    }

    /**
     * Manejo de excepciones específicas dentro del controlador.
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
        logger.error("Error: {}", ex.getMessage());
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Error de argumento invalido: {}", ex.getMessage());
        return ResponseEntity.status(400).body("Solicitud inválida: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        logger.error("Error inesperado: ", ex);
        return ResponseEntity.status(500).body("Error interno del servidor. Contáctese con EdwinSoporte.");
    }
}
