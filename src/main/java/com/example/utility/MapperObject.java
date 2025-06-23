package com.example.utility;

import com.example.dao.BuyerDao;
import com.example.dao.OrderProcessedDao;
import com.example.dao.ShoppingCartOrderDao;
import com.example.dto.BuyerDto;
import com.example.dto.OrderProcessedDto;
import com.example.dto.ShoppingCartOrderDto;
import com.example.model.Buyer;
import com.example.model.OrderProcessed;
import com.example.model.ShoppingCartOrder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class MapperObject {

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
        return buyer;
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
        dto.setPassAccount("confidencial");
        return dto;
    }


    // ------------------ SHOPPING CART ORDER ------------------

    // Model -> DAO
    private ShoppingCartOrderDao toDto(ShoppingCartOrder model) {
        ShoppingCartOrderDao dao = new ShoppingCartOrderDao();
        dao.setBuyerId(model.getBuyerId());
        dao.setProductId(model.getProductId());
        dao.setUnits(model.getUnits());
        dao.setTotal_product(model.getTotal_product());
        return dao;
    }

    // DAO -> Model
    private ShoppingCartOrder toModel(ShoppingCartOrderDao dao) {
        ShoppingCartOrder model = new ShoppingCartOrder();
        model.setBuyerId(dao.getBuyerId());
        model.setProductId(dao.getProductId());
        model.setUnits(dao.getUnits());
        model.setTotal_product(dao.getTotal_product());
        return model;
    }

    // DTO -> Model
    private ShoppingCartOrder toModel(ShoppingCartOrderDto dto) {
        ShoppingCartOrder model = new ShoppingCartOrder();
        model.setBuyerId(dto.getBuyerId());
        model.setProductId(dto.getProductId());
        model.setUnits(dto.getUnits());
        model.setTotal_product(dto.getTotal_product());
        return model;
    }

    // ------------------ ORDER PROCESSED ------------------

    // DAO -> Model
    private OrderProcessed toModel(OrderProcessedDao dao) {
        OrderProcessed model = new OrderProcessed();
        model.setPaymentId(dao.getPaymentId());
        model.setProductId(dao.getProductId());
        model.setUnits(dao.getUnits());
        model.setTotal_product(dao.getTotal_product());
        return model;
    }

    // Model -> DAO
    private OrderProcessedDao toDto(OrderProcessed model) {
        OrderProcessedDao dao = new OrderProcessedDao();
        dao.setPaymentId(model.getPaymentId());
        dao.setProductId(model.getProductId());
        dao.setUnits(model.getUnits());
        dao.setTotal_product(model.getTotal_product());
        return dao;
    }

    // DTO -> Model
    private OrderProcessed toModel(OrderProcessedDto dto) {
        OrderProcessed model = new OrderProcessed();
        model.setPaymentId(dto.getPaymentId());
        model.setProductId(dto.getProductId());
        model.setUnits(dto.getUnits());
        model.setTotal_product(dto.getTotal_product());
        return model;
    }
}



