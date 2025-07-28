package com.example.mapper;

import com.example.dao.OrderProcessedDao;
import com.example.dto.OrderProcessedDto;
import com.example.model.OrderProcessed;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@NoArgsConstructor
@Component
public class OrderProcessedMapper {

    // DTO -> Model
    public OrderProcessed toModel(OrderProcessedDto dto) {
        if (dto == null) return null;
        OrderProcessed model = new OrderProcessed();
        model.setPaymentId(dto.getPaymentId());
        model.setProductId(dto.getProductId());
        model.setUnits(dto.getUnits());
        model.setTotalProduct(dto.getTotalProduct());
        return model;
    }

    // Model -> DAO
    public OrderProcessedDao toDao(OrderProcessed model) {
        if (model == null) return null;
        OrderProcessedDao dao = new OrderProcessedDao();
        dao.setPaymentId(model.getPaymentId());
        dao.setProductId(model.getProductId());
        dao.setUnits(model.getUnits());
        dao.setTotalProduct(model.getTotalProduct());
        return dao;
    }

    // DAO -> Model
    public OrderProcessed toModel(OrderProcessedDao dao) {
        if (dao == null) return null;
        OrderProcessed model = new OrderProcessed();
        model.setPaymentId(dao.getPaymentId());
        model.setProductId(dao.getProductId());
        model.setUnits(dao.getUnits());
        model.setTotalProduct(dao.getTotalProduct());
        return model;
    }

    // Model -> DTO (toPublicDto)
    public OrderProcessedDto toDto(OrderProcessed model) {
        if (model == null) return null;
        OrderProcessedDto dto = new OrderProcessedDto();
        dto.setPaymentId(model.getPaymentId());
        dto.setProductId(model.getProductId());
        dto.setUnits(model.getUnits());
        dto.setTotalProduct(model.getTotalProduct());
        return dto;
    }

    // Actualización parcial del DAO
    public OrderProcessedDao partialUpdateToDao(OrderProcessedDao dao, Map<String, Object> updates) {

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "payment_id":
                    case "paymentId":
                        // if (value instanceof Number) dao.setPaymentId(((Number) value).intValue());
                        break;
                    case "product_id":
                    case "productId":
                        // if (value instanceof Number) dao.setProductId(((Number) value).intValue());
                        break;
                    case "units":
                        // if (value instanceof Number) dao.setUnits(((Number) value).intValue());
                        break;
                    case "total_product":
                    case "totalProduct":
                        // if (value instanceof Number) dao.setTotal_product(((Number) value).intValue());
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
