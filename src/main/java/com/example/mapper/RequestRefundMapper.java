package com.example.mapper;

import com.example.dao.RequestRefundDao;
import com.example.dto.RequestRefundDto;
import com.example.model.RequestRefund;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@NoArgsConstructor
@Component
public class RequestRefundMapper {

    // DTO -> Model
    public RequestRefund toModel(RequestRefundDto dto) {
        if (dto == null) return null;
        RequestRefund model = new RequestRefund();
        model.setId(dto.getId());
        model.setBuyerId(dto.getBuyerId());
        model.setPaymentId(dto.getPaymentId());
        model.setConfirmation(dto.getConfirmation());
        model.setRefundType(dto.getRefundType());
        return model;
    }

    // Model -> DAO
    public RequestRefundDao toDao(RequestRefund model) {
        if (model == null) return null;
        RequestRefundDao dao = new RequestRefundDao();
        dao.setId(model.getId());
        dao.setBuyerId(model.getBuyerId());
        dao.setPaymentId(model.getPaymentId());
        dao.setConfirmation(model.getConfirmation());
        dao.setRefundType(model.getRefundType());
        return dao;
    }

    // DAO -> Model
    public RequestRefund toModel(RequestRefundDao dao) {
        if (dao == null) return null;
        RequestRefund model = new RequestRefund();
        model.setId(dao.getId());
        model.setBuyerId(dao.getBuyerId());
        model.setPaymentId(dao.getPaymentId());
        model.setConfirmation(dao.getConfirmation());
        model.setRefundType(dao.getRefundType());
        return model;
    }

    // Model -> DTO (toPublicDto)
    public RequestRefundDto toPublicDto(RequestRefund model) {
        if (model == null) return null;
        RequestRefundDto dto = new RequestRefundDto();
        dto.setId(model.getId());
        dto.setBuyerId(model.getBuyerId());
        dto.setPaymentId(model.getPaymentId());
        dto.setConfirmation(model.getConfirmation());
        dto.setRefundType(model.getRefundType());
        return dto;
    }

    // Actualización parcial del DAO
    public RequestRefundDao parcialUpdateToDao(RequestRefundDao dao, Map<String, Object> updates) {

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "buyerId":
                    case "buyer_id":
                        if (value instanceof Number) dao.setBuyerId(((Number) value).intValue());
                        break;
                    case "paymentId":
                    case "payment_id":
                        if (value instanceof Number) dao.setPaymentId(((Number) value).intValue());
                        break;
                    case "confirmation":
                        if (value instanceof Number) dao.setConfirmation(((Number) value).intValue());
                        break;
                    case "refundType":
                    case "refund_type":
                        if (value instanceof Number) dao.setRefundType(((Number) value).intValue());
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
