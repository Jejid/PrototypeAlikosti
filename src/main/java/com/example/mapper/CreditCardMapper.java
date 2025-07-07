package com.example.mapper;

import com.example.dao.CreditCardDao;
import com.example.dto.CreditCardDto;
import com.example.model.CreditCard;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;


@NoArgsConstructor
@Component
public class CreditCardMapper {

    // DTO -> Model
    public CreditCard toModel(CreditCardDto dto) {
        if (dto == null) return null;
        CreditCard model = new CreditCard();
        model.setId(dto.getId());
        model.setBuyerId(dto.getBuyerId());
        model.setName(dto.getName());
        model.setCardNumber(dto.getCardNumber());
        model.setCardDate(dto.getCardDate());
        model.setCvcCode(dto.getCvcCode());
        model.setTokenizedCode(dto.getTokenizedCode());
        model.setBank(dto.getBank());
        model.setCardType(dto.getCardType());
        model.setFranchise(dto.getFranchise());
        return model;
    }

    // Model -> DAO
    public CreditCardDao toDao(CreditCard model) {
        if (model == null) return null;
        CreditCardDao dao = new CreditCardDao();
        dao.setId(model.getId());
        dao.setBuyerId(model.getBuyerId());
        dao.setName(model.getName());
        dao.setCardNumber(model.getCardNumber());
        dao.setCardDate(model.getCardDate());
        dao.setCvcCode(model.getCvcCode());
        dao.setTokenizedCode(model.getTokenizedCode());
        dao.setBank(model.getBank());
        dao.setCardType(model.getCardType());
        dao.setFranchise(model.getFranchise());
        return dao;
    }

    // DAO -> Model
    public CreditCard toModel(CreditCardDao dao) {
        if (dao == null) return null;
        CreditCard model = new CreditCard();
        model.setId(dao.getId());
        model.setBuyerId(dao.getBuyerId());
        model.setName(dao.getName());
        model.setCardNumber(dao.getCardNumber());
        model.setCardDate(dao.getCardDate());
        model.setCvcCode(dao.getCvcCode());
        model.setTokenizedCode(dao.getTokenizedCode());
        model.setBank(dao.getBank());
        model.setCardType(dao.getCardType());
        model.setFranchise(dao.getFranchise());
        return model;
    }

    // Model -> DTO
    public CreditCardDto toPublicDto(CreditCard model) {
        if (model == null) return null;
        CreditCardDto dto = new CreditCardDto();
        dto.setId(model.getId());
        dto.setBuyerId(model.getBuyerId());
        dto.setName(model.getName());
        dto.setCardNumber("**** **** **** " + model.getCardNumber().substring(model.getCardNumber().length() - 4)); // se toman los ultimos 4 digitos
        dto.setCardDate("confidencial");
        dto.setCvcCode(0);
        dto.setTokenizedCode(model.getTokenizedCode());
        dto.setBank(model.getBank());
        dto.setCardType(model.getCardType());
        dto.setFranchise(model.getFranchise());
        return dto;
    }

    // Actualización parcial del DAO
    public CreditCardDao parcialUpdateToDao(CreditCardDao dao, Map<String, Object> updates) {

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "buyerId":
                        if (value instanceof Number) dao.setBuyerId(((Number) value).intValue());
                        break;
                    case "name":
                        if (value instanceof String) dao.setName((String) value);
                        break;
                    case "cardNumber":
                        if (value instanceof String) dao.setCardNumber((String) value);
                        break;
                    case "cardDate":
                        if (value instanceof String) dao.setCardDate((String) value);
                        break;
                    case "cvcCode":
                        if (value instanceof Number) dao.setCvcCode(((Number) value).intValue());
                        break;
                    case "tokenizedCode":
                        if (value instanceof String) dao.setTokenizedCode((String) value);
                        break;
                    case "bank":
                        if (value instanceof String) dao.setBank((String) value);
                        break;
                    case "cardType":
                        if (value instanceof String) dao.setCardType((String) value);
                        break;
                    case "franchise":
                        if (value instanceof String) dao.setFranchise((String) value);
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
