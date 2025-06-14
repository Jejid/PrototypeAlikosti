package com.example.runner;


import com.example.repository.ProductRepository;
import org.springframework.stereotype.Component;


@Component
public class DataBaseTestOne {

    private final ProductRepository productRepository;

    public DataBaseTestOne(ProductRepository userRepository) {
        this.productRepository = userRepository;
    }

    /*@Override
    public void run(String... args) {
        List<ProductDao> productsDao = productRepository.findAll();
        System.out.println("Productos en la base de datos: " + productsDao.size());

        productsDao.forEach(product ->
                System.out.println("ID: " + product.getId() + " | Nombre: " + product.getName() + " | Price: " + product.getPrice())
        );


    }*/
}