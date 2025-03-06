package com.example.runner;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataBaseTestOne implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataBaseTestOne(ProductRepository userRepository) {
        this.productRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        List<Product> users = productRepository.findAll();
        System.out.println("Productos en la base de datos: " + users.size());

        users.forEach(product ->
                System.out.println("ID: " + product.getId() + " | Nombre: " + product.getName() + " | Price: " + product.getPrice())
        );
    }
}
