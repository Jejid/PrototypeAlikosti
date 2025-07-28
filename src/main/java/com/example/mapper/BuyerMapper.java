package com.example.mapper;

import com.example.dao.BuyerDao;
import com.example.dto.BuyerDto;
import com.example.model.Buyer;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@NoArgsConstructor
@Component
public class BuyerMapper {

    // ------------------ BUYER ------------------

    // DTO -> Model
    public Buyer toModel(BuyerDto dto) {
        if (dto == null) return null;
        Buyer model = new Buyer();
        model.setId(dto.getId());
        model.setName(dto.getName());
        model.setSurname(dto.getSurname());
        model.setBirthDate(dto.getBirthDate());
        model.setCc(dto.getCc());
        model.setEmail(dto.getEmail());
        model.setPassAccount(dto.getPassAccount());
        model.setPhone(dto.getPhone());
        return model;
    }

    // Model -> DAO
    public BuyerDao toDao(Buyer model) {
        if (model == null) return null;
        BuyerDao dao = new BuyerDao();
        dao.setId(model.getId());
        dao.setName(model.getName());
        dao.setSurname(model.getSurname());
        dao.setBirthDate(model.getBirthDate());
        dao.setCc(model.getCc());
        dao.setEmail(model.getEmail());
        dao.setPassAccount(model.getPassAccount());
        dao.setPhone(model.getPhone());
        return dao;
    }

    // DAO -> Model
    public Buyer toModel(BuyerDao dao) {
        if (dao == null) return null;
        Buyer buyer = new Buyer();
        buyer.setId(dao.getId());
        buyer.setName(dao.getName());
        buyer.setSurname(dao.getSurname());
        buyer.setBirthDate(dao.getBirthDate());
        buyer.setCc(dao.getCc());
        buyer.setEmail(dao.getEmail());
        buyer.setPassAccount(dao.getPassAccount());
        buyer.setPhone(dao.getPhone());
        return buyer;
    }

    // Model -> DTO
    public BuyerDto toDto(Buyer buyer) {
        if (buyer == null) return null;
        BuyerDto dto = new BuyerDto();
        dto.setId(buyer.getId());
        dto.setName(buyer.getName());
        dto.setSurname(buyer.getSurname());
        dto.setBirthDate(buyer.getBirthDate());
        dto.setEmail(buyer.getEmail());
        dto.setCc(buyer.getCc());
        //dto.setPassAccount(buyer.getPassAccount());
        dto.setPhone(buyer.getPhone());
        return dto;
    }

    // Model -> DTO
    public BuyerDto toPublicDto(Buyer buyer) {
        if (buyer == null) return null;
        BuyerDto dto = new BuyerDto();
        dto.setId(buyer.getId());
        dto.setName(buyer.getName());
        dto.setSurname(buyer.getSurname());
        dto.setBirthDate(buyer.getBirthDate());
        dto.setEmail(buyer.getEmail());
        dto.setCc("confidencial");
        dto.setPassAccount("**********");
        dto.setPhone(buyer.getPhone());
        return dto;
    }

    public BuyerDao parcialUpdateToDao(BuyerDao dao, Map<String, Object> updates) {

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "name":
                        if (value == null || value instanceof String) dao.setName((String) value);
                        break;
                    case "surname":
                        if (value == null || value instanceof String) dao.setSurname((String) value);
                        break;
                    case "birthDate":
                        if (value == null || value instanceof String) dao.setBirthDate(((String) value));
                        break;
                    case "cc":
                        if (value == null || value instanceof String) dao.setCc(((String) value));
                        break;
                    case "email":
                        if (value == null || value instanceof String) dao.setEmail((String) value);
                        break;
                    case "passAccount":
                        if (value == null || value instanceof String) dao.setPassAccount(((String) value));
                        break;
                    case "phone":
                        if (value == null || value instanceof String) dao.setPhone(((String) value));
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

    /*public BuyerDao parcialUpdateToDao2(BuyerDto dto, BuyerDao daoOrigin) {
        if (dto == null) return null;

        if (dto.getName() != null) daoOrigin.setName(dto.getName());
        if (dto.getSurname() != null) daoOrigin.setSurname(dto.getSurname());
        if (dto.getBirthDate() != null) daoOrigin.setBirthDate(dto.getBirthDate());
        if (dto.getCc() != null) daoOrigin.setCc(dto.getCc());
        if (dto.getEmail() != null) daoOrigin.setEmail(dto.getEmail());
        if (dto.getPassAccount() != null) daoOrigin.setPassAccount(dto.getPassAccount());

        return daoOrigin;
    }*/
}



