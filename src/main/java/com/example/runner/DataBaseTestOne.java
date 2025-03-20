package com.example.runner;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataBaseTestOne  {

    private final ProductRepository productRepository;

    public DataBaseTestOne(ProductRepository userRepository) {
        this.productRepository = userRepository;
    }

    /*
    @Override
    public void run(String... args) {
        List<Product> products = productRepository.findAll();
        System.out.println("Productos en la base de datos: " + products.size());

        products.forEach(product ->
                System.out.println("ID: " + product.getId() + " | Nombre: " + product.getName() + " | Price: " + product.getPrice())
        );
        */

    }
