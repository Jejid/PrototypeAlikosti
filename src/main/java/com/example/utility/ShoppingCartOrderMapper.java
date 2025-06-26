package com.example.utility;

import com.example.dao.ShoppingCartOrderDao;
import com.example.dto.ShoppingCartOrderDto;
import com.example.model.ShoppingCartOrder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@NoArgsConstructor
@Component
public class ShoppingCartOrderMapper {

    // DTO -> Model
    public ShoppingCartOrder toModel(ShoppingCartOrderDto dto) {
        if (dto == null) return null;
        ShoppingCartOrder model = new ShoppingCartOrder();
        model.setBuyerId(dto.getBuyerId());
        model.setProductId(dto.getProductId());
        model.setUnits(dto.getUnits());
        model.setTotalProduct(dto.getTotalProduct());
        return model;
    }

    // Model -> DAO
    public ShoppingCartOrderDao toDao(ShoppingCartOrder model) {
        if (model == null) return null;
        ShoppingCartOrderDao dao = new ShoppingCartOrderDao();
        dao.setBuyerId(model.getBuyerId());
        dao.setProductId(model.getProductId());
        dao.setUnits(model.getUnits());
        dao.setTotalProduct(model.getTotalProduct());
        return dao;
    }

    // DAO -> Model
    public ShoppingCartOrder toModel(ShoppingCartOrderDao dao) {
        if (dao == null) return null;
        ShoppingCartOrder model = new ShoppingCartOrder();
        model.setBuyerId(dao.getBuyerId());
        model.setProductId(dao.getProductId());
        model.setUnits(dao.getUnits());
        model.setTotalProduct(dao.getTotalProduct());
        return model;
    }

    // Model -> DTO (toPublicDto)
    public ShoppingCartOrderDto toPublicDto(ShoppingCartOrder model) {
        if (model == null) return null;
        ShoppingCartOrderDto dto = new ShoppingCartOrderDto();
        dto.setBuyerId(model.getBuyerId());
        dto.setProductId(model.getProductId());
        dto.setUnits(model.getUnits());
        dto.setTotalProduct(model.getTotalProduct());
        return dto;
    }

    // Actualización parcial del DAO
    public ShoppingCartOrderDao partialUpdateToDao(ShoppingCartOrderDao dao, Map<String, Object> updates) {

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "buyer_id":
                    case "buyerId":
                        //if (value instanceof Number) dao.setBuyerId(((Number) value).intValue());
                        break;
                    case "product_id":
                    case "productId":
                        //if (value instanceof Number) dao.setProductId(((Number) value).intValue());
                        break;
                    case "units":
                        //if (value instanceof Number) dao.setUnits(((Number) value).intValue());
                        break;
                    case "total_product":
                    case "totalProduct":
                        //if (value instanceof Number) dao.setTotal_product(((Number) value).intValue());
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
