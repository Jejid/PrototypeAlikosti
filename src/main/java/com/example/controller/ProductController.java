package com.example.controller;

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
import java.util.stream.Collectors;


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
        List<Product> productList = productService.getAllProducts();
        List<ProductDto> productListDtos = new ArrayList<>();

        for (Product product : productList) {
            productListDtos.add(toDto(product));
        }
        return new ResponseEntity<>(productListDtos,HttpStatus.OK);
    }

    @GetMapping("store/{storeId}/category/{categoryId}")
    public ResponseEntity<List<ProductDto>> getProductsByCategoryStore(@PathVariable int storeId, @PathVariable int categoryId){
        List<Product> productList = productService.getProductByCategoryStore(storeId,categoryId);
        return new ResponseEntity<>(productList.stream().map(this::toDto).collect(Collectors.toList()),HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id) {

        Product product = productService.getProductById(id);

        return new ResponseEntity<>(toDto(product),HttpStatus.OK);
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
        response.put("message", "Producto: " + updatedProduct.getName()+ ", campo/s actualizado/s exitosamente");
        return ResponseEntity.ok(response);
    }

    // metodo auxiliar para mapear modelo a DTO
    private ProductDto toDto(Product model) {
        ProductDto dto = new ProductDto();
        dto.setStock(model.getStoreId());
        dto.setCategoryId(model.getCategoryId());
        dto.setName(model.getName());
        dto.setPrice(model.getPrice());
        dto.setDescription(model.getDescription());
        dto.setStock(model.getStock());
        dto.setPic(model.getPic());
        return dto;
    }

}
