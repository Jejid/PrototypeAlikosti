package com.example.service;

import com.example.dao.ProductDao;
import com.example.dto.ProductDto;
import com.example.exception.EntityNotFoundException;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {

        List<ProductDao> productListDao = productRepository.findAll();
        List<Product> productList = new ArrayList<>();

        for (ProductDao productDao : productListDao) {
            Product product = new Product();
            product.setId(productDao.getId());
            product.setStoreId(productDao.getStoreId());
            product.setName(productDao.getName());
            product.setCategoryId(productDao.getCategoryId());
            product.setPrice(productDao.getPrice());
            product.setStock(productDao.getStock());
            product.setDescription(productDao.getDescription());
            product.setPic(productDao.getPic());
            productList.add(product);
        }
        return productList;
    }


    public Product getProductById(Integer id) {

        Optional<ProductDao> productDao = productRepository.findById(id);

        ProductDao productDao1 = productDao.orElseThrow(() -> new EntityNotFoundException("Producto con ID: " + id + ", no encontrado"));
        Product product = new Product();
        product.setId(productDao1.getId());
        product.setStoreId(productDao1.getStoreId());
        product.setCategoryId(productDao1.getCategoryId());
        product.setName(productDao1.getName());
        product.setPrice(productDao1.getPrice());
        product.setDescription(productDao1.getDescription());
        product.setStock(productDao1.getStock());
        product.setPic(productDao1.getPic());

        return product;
    }

    public ProductDao createProduct(Product product) {
        
        ProductDao productDao = new ProductDao();
        productDao.setName(product.getName());
        productDao.setStoreId(1);
        productDao.setCategoryId(product.getCategoryId());
        productDao.setPrice(product.getPrice());
        productDao.setStock(product.getStock());
        productDao.setDescription(product.getDescription());
        productDao.setPic(product.getPic());

        return productRepository.save(productDao);
    }
    public void deleteProduct(Integer id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Producto con ID " + id + ", no encontrado");
        }
        productRepository.deleteById(id);
    }

/*
    public Product updateProduct(Integer id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setDescription(updatedProduct.getDescription());
                    product.setPrice(updatedProduct.getPrice());
                    product.setStock(updatedProduct.getStock());
                    product.setPic(updatedProduct.getPic());
                    product.setCategoryId(updatedProduct.getCategoryId());
                    product.setStoreId(updatedProduct.getStoreId());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

   */
}
