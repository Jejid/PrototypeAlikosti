package com.example.utility;

import com.example.dao.ProductDao;
import com.example.dto.ProductDto;
import com.example.model.Product;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@NoArgsConstructor
@Component
public class ProductMapper {

    // DTO -> Model
    public Product toModel(ProductDto dto) {
        if (dto == null) return null;
        Product model = new Product();
        model.setId(dto.getId());
        model.setCategoryId(dto.getCategoryId());
        model.setName(dto.getName());
        model.setPrice(dto.getPrice());
        model.setDescription(dto.getDescription());
        model.setStock(dto.getStock());
        model.setPic(dto.getPic());
        model.setStoreId(dto.getStoreId());
        return model;
    }

    // Model -> DAO
    public ProductDao toDao(Product model) {
        if (model == null) return null;
        ProductDao dao = new ProductDao();
        dao.setId(model.getId());
        dao.setCategoryId(model.getCategoryId());
        dao.setName(model.getName());
        dao.setPrice(model.getPrice());
        dao.setDescription(model.getDescription());
        dao.setStock(model.getStock());
        dao.setPic(model.getPic());
        dao.setStoreId(model.getStoreId());
        return dao;
    }

    // DAO -> Model
    public Product toModel(ProductDao dao) {
        if (dao == null) return null;
        Product model = new Product();
        model.setId(dao.getId());
        model.setCategoryId(dao.getCategoryId());
        model.setName(dao.getName());
        model.setPrice(dao.getPrice());
        model.setDescription(dao.getDescription());
        model.setStock(dao.getStock());
        model.setPic(dao.getPic());
        model.setStoreId(dao.getStoreId());
        return model;
    }

    // Model -> DTO
    public ProductDto toPublicDto(Product model) {
        if (model == null) return null;
        ProductDto dto = new ProductDto();
        dto.setId(model.getId());
        dto.setCategoryId(model.getCategoryId());
        dto.setName(model.getName());
        dto.setPrice(model.getPrice());
        dto.setDescription(model.getDescription());
        dto.setStock(model.getStock());
        dto.setPic(model.getPic());
        dto.setStoreId(model.getStoreId());
        return dto;
    }

    // Actualización parcial del DAO
    public ProductDao parcialUpdateToDao(ProductDao dao, Map<String, Object> updates) {

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "categoryId":
                        if (value instanceof Number) dao.setCategoryId(((Number) value).intValue());
                        break;
                    case "name":
                        if (value instanceof String) dao.setName((String) value);
                        break;
                    case "price":
                        if (value instanceof Number) dao.setPrice(((Number) value).intValue());
                        break;
                    case "description":
                        if (value instanceof String) dao.setDescription((String) value);
                        break;
                    case "stock":
                        if (value instanceof Number) dao.setStock(((Number) value).intValue());
                        break;
                    case "pic":
                        if (value instanceof String) dao.setPic((String) value);
                        break;
                    case "storeId":
                        if (value instanceof Number) dao.setStoreId(((Number) value).intValue());
                        break;
                    default:
                        throw new IllegalArgumentException("El campo " + key + " no es válido o no existe para actualización.");
                }
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Tipo de dato incorrecto para el campo " + key);
            }
        });

        return dao;
    }
}
