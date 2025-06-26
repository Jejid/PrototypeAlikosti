package com.example.mapper;

import com.example.dao.ProductCategoryDao;
import com.example.dto.ProductCategoryDto;
import com.example.model.ProductCategory;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@NoArgsConstructor
@Component
public class ProductCategoryMapper {

    // DTO -> Model
    public ProductCategory toModel(ProductCategoryDto dto) {
        if (dto == null) return null;
        ProductCategory model = new ProductCategory();
        model.setId(dto.getId());
        model.setStoreId(dto.getStoreId());
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        return model;
    }

    // Model -> DAO
    public ProductCategoryDao toDao(ProductCategory model) {
        if (model == null) return null;
        ProductCategoryDao dao = new ProductCategoryDao();
        dao.setId(model.getId());
        dao.setStoreId(model.getStoreId());
        dao.setName(model.getName());
        dao.setDescription(model.getDescription());
        return dao;
    }

    // DAO -> Model
    public ProductCategory toModel(ProductCategoryDao dao) {
        if (dao == null) return null;
        ProductCategory model = new ProductCategory();
        model.setId(dao.getId());
        model.setStoreId(dao.getStoreId());
        model.setName(dao.getName());
        model.setDescription(dao.getDescription());
        return model;
    }

    // Model -> DTO
    public ProductCategoryDto toPublicDto(ProductCategory model) {
        if (model == null) return null;
        ProductCategoryDto dto = new ProductCategoryDto();
        dto.setId(model.getId());
        dto.setStoreId(model.getStoreId());
        dto.setName(model.getName());
        dto.setDescription(model.getDescription());
        return dto;
    }

    // Actualización parcial del DAO
    public ProductCategoryDao parcialUpdateToDao(ProductCategoryDao dao, Map<String, Object> updates) {

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "store_id":
                        if (value instanceof Number) dao.setStoreId(((Number) value).intValue());
                        break;
                    case "name":
                        if (value instanceof String) dao.setName((String) value);
                        break;
                    case "description":
                        if (value instanceof String) dao.setDescription((String) value);
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
