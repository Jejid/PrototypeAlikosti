package com.example.service;

import com.example.dao.ProductCategoryDao;
import com.example.exception.EntityNotFoundException;
import com.example.model.ProductCategory;
import com.example.repository.ProductCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public List<ProductCategory> getAllProductCategories() {
        List<ProductCategoryDao> daoList = productCategoryRepository.findAll();
        List<ProductCategory> categoryList = new ArrayList<>();

        for (ProductCategoryDao dao : daoList) {
            categoryList.add(new ProductCategory(
                    dao.getId(),
                    dao.getStoreId(),
                    dao.getName(),
                    dao.getDescription()
            ));
        }
        return categoryList;
    }

    public ProductCategory getProductCategoryById(Integer id) {
        ProductCategoryDao dao = productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría con ID: " + id + " no encontrada"));

        return new ProductCategory(
                dao.getId(),
                dao.getStoreId(),
                dao.getName(),
                dao.getDescription()
        );
    }

    public ProductCategory createProductCategory(ProductCategory category) {
        ProductCategoryDao dao = new ProductCategoryDao();
        dao.setStoreId(category.getStoreId());
        dao.setName(category.getName());
        dao.setDescription(category.getDescription());

        ProductCategoryDao saved = productCategoryRepository.save(dao);

        return new ProductCategory(
                saved.getId(),
                saved.getStoreId(),
                saved.getDescription(), saved.getName()
        );
    }

    public ProductCategory updateProductCategory(Integer id, ProductCategory updated) {
        ProductCategoryDao dao = productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría con ID: " + id + " no encontrada"));

        dao.setStoreId(updated.getStoreId());
        dao.setName(updated.getName());
        dao.setDescription(updated.getDescription());

        ProductCategoryDao saved = productCategoryRepository.save(dao);

        return new ProductCategory(
                saved.getId(),
                saved.getStoreId(),
                saved.getName(),
                saved.getDescription()
        );
    }

    public void deleteProductCategory(Integer id) {
        if (productCategoryRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Categoría con ID " + id + " no encontrada");
        }
        productCategoryRepository.deleteById(id);
    }

    public ProductCategory partialUpdateProductCategory(Integer id, Map<String, Object> updates) {
        ProductCategoryDao dao = productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría con ID: " + id + " no encontrada"));

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "storeId" -> {
                        if (value instanceof Number) dao.setStoreId(((Number) value).intValue());
                    }
                    case "name" -> {
                        if (value == null || value instanceof String) dao.setName((String) value);
                    }
                    case "description" -> {
                        if (value == null || value instanceof String) dao.setDescription((String) value);
                    }
                    default -> throw new IllegalArgumentException("Campo " + key + " no válido para actualización.");
                }
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Tipo de dato incorrecto para el campo " + key);
            }
        });

        ProductCategoryDao saved = productCategoryRepository.save(dao);

        return new ProductCategory(
                saved.getId(),
                saved.getStoreId(),
                saved.getName(),
                saved.getDescription()
        );
    }
}
