package com.example.mapper;

import com.example.dao.PaymentDao;
import com.example.dto.PaymentDto;
import com.example.model.Payment;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@NoArgsConstructor
@Component
public class PaymentMapper {

    // DTO -> Model
    public Payment toModel(PaymentDto dto) {
        if (dto == null) return null;
        Payment model = new Payment();
        model.setId(dto.getId());
        model.setBuyerId(dto.getBuyerId());
        model.setPaymentMethodId(dto.getPaymentMethodId());
        model.setTotalOrder(dto.getTotalOrder());
        model.setDate(dto.getDate());
        model.setConfirmation(dto.getConfirmation());
        model.setCodeConfirmation(dto.getCodeConfirmation());
        model.setCardNumber(dto.getCardNumber());
        model.setRefunded(dto.isRefunded());
        model.setPaymentGatewayOrderId(dto.getPaymentGatewayOrderId());
        model.setPaymentGatewayTransactionId(dto.getPaymentGatewayTransactionId());
        return model;
    }

    // Model -> DAO
    public PaymentDao toDao(Payment model) {
        if (model == null) return null;
        PaymentDao dao = new PaymentDao();
        dao.setId(model.getId());
        dao.setBuyerId(model.getBuyerId());
        dao.setPaymentMethodId(model.getPaymentMethodId());
        dao.setTotalOrder(model.getTotalOrder());
        dao.setDate(model.getDate());
        dao.setConfirmation(model.getConfirmation());
        dao.setCodeConfirmation(model.getCodeConfirmation());
        dao.setCardNumber(model.getCardNumber());
        dao.setRefunded(model.isRefunded());
        dao.setPaymentGatewayOrderId(model.getPaymentGatewayOrderId());
        dao.setPaymentGatewayTransactionId(model.getPaymentGatewayTransactionId());
        return dao;
    }

    // DAO -> Model
    public Payment toModel(PaymentDao dao) {
        if (dao == null) return null;
        Payment model = new Payment();
        model.setId(dao.getId());
        model.setBuyerId(dao.getBuyerId());
        model.setPaymentMethodId(dao.getPaymentMethodId());
        model.setTotalOrder(dao.getTotalOrder());
        model.setDate(dao.getDate());
        model.setConfirmation(dao.getConfirmation());
        model.setCodeConfirmation(dao.getCodeConfirmation());
        model.setCardNumber(dao.getCardNumber());
        model.setRefunded(dao.isRefunded());
        model.setPaymentGatewayOrderId(dao.getPaymentGatewayOrderId());
        model.setPaymentGatewayTransactionId(dao.getPaymentGatewayTransactionId());
        return model;
    }

    // Model -> DTO
    public PaymentDto toPublicDto(Payment model) {
        if (model == null) return null;
        PaymentDto dto = new PaymentDto();
        dto.setId(model.getId());
        dto.setBuyerId(model.getBuyerId());
        dto.setPaymentMethodId(model.getPaymentMethodId());
        dto.setTotalOrder(model.getTotalOrder());
        dto.setDate(model.getDate());
        dto.setConfirmation(model.getConfirmation());
        dto.setCodeConfirmation(model.getCodeConfirmation());

        if (model.getCardNumber() != null)
            if (model.getCardNumber().length() < 20)
                dto.setCardNumber("**** **** **** " + model.getCardNumber().substring(model.getCardNumber().length() - 4));
            else
                dto.setCardNumber(model.getPaymentGatewayOrderId() == null ? null : "Token de tarjeta: ***...***" + model.getCardNumber().substring(model.getCardNumber().length() - 6));

        dto.setRefunded(model.isRefunded());
        dto.setPaymentGatewayOrderId(model.getPaymentGatewayOrderId());
        dto.setPaymentGatewayTransactionId(model.getPaymentGatewayTransactionId());
        return dto;
    }

    // Actualización parcial del DAO
    public PaymentDao parcialUpdateToDao(PaymentDao dao, Map<String, Object> updates) {

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "buyerId":
                    case "buyer_id":
                        if (value instanceof Number) dao.setBuyerId(((Number) value).intValue());
                        break;
                    case "paymentMethodID":
                    case "payment_method_id":
                        if (value instanceof Number) dao.setPaymentMethodId(((Number) value).intValue());
                        break;
                    case "totalOrder":
                    case "total_order":
                        if (value instanceof Number) dao.setTotalOrder(((Number) value).intValue());
                        break;
                    case "date":
                        if (value instanceof String) dao.setDate((String) value);
                        break;
                    case "confirmation":
                        if (value instanceof Number) dao.setConfirmation(0);
                        break;
                    case "codeConfirmation":
                    case "code_confirmation":
                        if (value == null || value instanceof String)
                            dao.setCodeConfirmation(value == null ? null : ((String) value));
                        break;
                    case "cardNumber":
                    case "card_number":
                        if (value instanceof String) dao.setCardNumber((String) value);
                        break;
                    case "refunded":
                        if (value instanceof Boolean) dao.setRefunded((Boolean) value);
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
