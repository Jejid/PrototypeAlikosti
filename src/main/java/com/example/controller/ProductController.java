package com.example.controller;

import com.example.dto.ProductDto;
import com.example.exception.EntityNotFoundException;
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

        Product productCreated = productService.createProduct(product);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Producto: " + productCreated.getName() + " creado exitosamente");

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
        //logger.info("Intentando actualizar el producto con ID: {}", id);
        Product updatedProduct = productService.updateProduct(id, product);
        //logger.info("Producto actualizado correctamente: {}", updatedProduct);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Producto: " + updatedProduct.getName()+ ", actualizado exitosamente");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity <Map<String, String>> partialUpdateProduct(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        Product updatedProduct = productService.partialUpdateProduct(id, updates);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Producto: " + updatedProduct.getName()+ ", campos actualizados exitosamente");
        return ResponseEntity.ok(response);
    }


}
