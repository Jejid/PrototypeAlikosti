package com.example.controller;


import com.example.dao.ProductDao;
import com.example.dto.ProductDto;
import com.example.model.Product;
import com.example.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public ResponseEntity<List<ProductDto>> getAllProducts() {

        List<ProductDto> productListDtos = new ArrayList<>();
        List<Product> productList = productService.getAllProducts();
        for (Product product : productList) {
            ProductDto productDto = new ProductDto();
            //productDto.setId(product.getId());
            productDto.setName(product.getName());
            //productDto.setStoreId(product.getStoreId());
            productDto.setCategoryId(product.getCategoryId());
            productDto.setPrice(product.getPrice());
            productDto.setStock(product.getStock());
            productDto.setDescription(product.getDescription());
            productDto.setPic(product.getPic());
            productListDtos.add(productDto);
        }
        return new ResponseEntity<>(productListDtos,HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id) {

        Product product = productService.getProductById(id);

        ProductDto productDto = new ProductDto();

        //productDto.setId(product.getId());
        productDto.setName(product.getName());
        //productDto.setStoreId(product.getStoreId());
        productDto.setCategoryId(product.getCategoryId());
        productDto.setPrice(product.getPrice());
        productDto.setStock(product.getStock());
        productDto.setDescription(product.getDescription());
        productDto.setPic(product.getPic());

        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Map<String, String>> createProduct(@Valid @RequestBody Product product) {

        productService.createProduct(product);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Producto: " + product.getName() + " creado exitosamente");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Integer id) {

        String nameProduct = productService.getProductById(id).getName();
        productService.deleteProduct(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Producto: " +nameProduct+ " eliminado exitosamente");
        return ResponseEntity.ok(response);
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateProduct(@PathVariable Integer id,@Valid @RequestBody Product product) {
        logger.info("Intentando actualizar el producto con ID: {}", id);
        Product updatedProduct = productService.updateProduct(id, product);
        logger.info("Producto actualizado correctamente: {}", updatedProduct);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Producto: " + product.getName() + ", actulizado exitosamente");
        return ResponseEntity.ok(response);
    }
/*
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

*/
}
