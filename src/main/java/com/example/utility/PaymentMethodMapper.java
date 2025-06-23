package com.example.utility;

import com.example.dao.PaymentMethodDao;
import com.example.dto.PaymentMethodDto;
import com.example.model.PaymentMethod;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@NoArgsConstructor
@Component
public class PaymentMethodMapper {

    // DTO -> Model
    public PaymentMethod toModel(PaymentMethodDto dto) {
        if (dto == null) return null;
        PaymentMethod model = new PaymentMethod();
        model.setId(dto.getId());
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        return model;
    }

    // Model -> DAO
    public PaymentMethodDao toDao(PaymentMethod model) {
        if (model == null) return null;
        PaymentMethodDao dao = new PaymentMethodDao();
        dao.setId(model.getId());
        dao.setName(model.getName());
        dao.setDescription(model.getDescription());
        return dao;
    }

    // DAO -> Model
    public PaymentMethod toModel(PaymentMethodDao dao) {
        if (dao == null) return null;
        PaymentMethod model = new PaymentMethod();
        model.setId(dao.getId());
        model.setName(dao.getName());
        model.setDescription(dao.getDescription());
        return model;
    }

    // Model -> DTO
    public PaymentMethodDto toPublicDto(PaymentMethod model) {
        if (model == null) return null;
        PaymentMethodDto dto = new PaymentMethodDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setDescription(model.getDescription());
        return dto;
    }

    // Actualización parcial del DAO
    public PaymentMethodDao parcialUpdateToDao(PaymentMethodDao dao, Map<String, Object> updates) {

        updates.forEach((key, value) -> {
            try {
                switch (key) {
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
