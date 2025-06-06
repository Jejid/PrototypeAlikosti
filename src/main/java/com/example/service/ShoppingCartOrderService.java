package com.example.service;

import com.example.dao.ShoppingCartOrderDao;
import com.example.key.ShoppingCartOrderKey;
import com.example.model.ShoppingCartOrder;
import com.example.repository.ShoppingCartOrderRepository;
import com.example.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@Service
public class ShoppingCartOrderService {

    private final ShoppingCartOrderRepository repository;

    public ShoppingCartOrderService(ShoppingCartOrderRepository repository) {
        this.repository = repository;
    }

    public List<ShoppingCartOrder> getAllItemOrders() {
        List<ShoppingCartOrderDao> daoList = repository.findAll();
        return daoList.stream().map(this::toModel).collect(Collectors.toList());
    }

    public ShoppingCartOrder getItemOrderById(int buyerId, int productId) {
        ShoppingCartOrderDao dao = repository.findById(new ShoppingCartOrderKey(buyerId, productId))
                .orElseThrow(() -> new EntityNotFoundException("Item de la orden con buyerId: "+buyerId+" productID: "+productId+" no existe"));
        return toModel(dao);
    }

    public ShoppingCartOrder createItemOrder(ShoppingCartOrder order) {
        ShoppingCartOrderKey key = new ShoppingCartOrderKey(order.getBuyerId(), order.getProductId());

        if (repository.existsById(key)) {
            throw new IllegalArgumentException("El item con buyerId " + order.getBuyerId() +
                    " y productId " + order.getProductId() + " ya existe.");
        }

        ShoppingCartOrderDao dao = toDao(order);
        ShoppingCartOrderDao saved = repository.save(dao);
        return toModel(saved);
    }

    public ShoppingCartOrder updateItemOrder(int buyerId, int productId, ShoppingCartOrder order) {
        ShoppingCartOrderKey key = new ShoppingCartOrderKey(buyerId, productId);
        ShoppingCartOrderDao existingDao = repository.findById(key)
                .orElseThrow(() -> new EntityNotFoundException("Item de la orden con buyerId: "+buyerId+" productID: "+productId+" no existe"));

        existingDao.setUnits(order.getUnits());
        existingDao.setTotal(order.getTotal());

        ShoppingCartOrderDao saved = repository.save(existingDao);
        return toModel(saved);
    }

    public void deleteItemOrder(int buyerId, int productId) {
        ShoppingCartOrderKey key = new ShoppingCartOrderKey(buyerId, productId);
        if (!repository.existsById(key)) {
            throw new EntityNotFoundException("Item de la orden con buyerId: "+buyerId+" productID: "+productId+" no existe");
        }
        repository.deleteById(key);
    }

    public ShoppingCartOrder partialUpdateItemOrder(int buyerId, int productId, Map<String, Object> updates) {
        ShoppingCartOrderKey key = new ShoppingCartOrderKey(buyerId, productId);
        ShoppingCartOrderDao existingDao = repository.findById(key)
                .orElseThrow(() -> new EntityNotFoundException("Item de la orden con buyerId: "+buyerId+" productID: "+productId+" no existe"));

        if (updates.containsKey("units")) {
            existingDao.setUnits((Integer) updates.get("units"));
        }
        if (updates.containsKey("total")) {
            existingDao.setTotal((Integer) updates.get("total"));
        }

        ShoppingCartOrderDao saved = repository.save(existingDao);
        return toModel(saved);
    }

    // Conversiones DAO <-> Model
    private ShoppingCartOrder toModel(ShoppingCartOrderDao dao) {
        ShoppingCartOrder model = new ShoppingCartOrder();
        model.setBuyerId(dao.getBuyerId());
        model.setProductId(dao.getProductId());
        model.setUnits(dao.getUnits());
        model.setTotal(dao.getTotal());
        return model;
    }

    private ShoppingCartOrderDao toDao(ShoppingCartOrder model) {
        ShoppingCartOrderDao dao = new ShoppingCartOrderDao();
        dao.setBuyerId(model.getBuyerId());
        dao.setProductId(model.getProductId());
        dao.setUnits(model.getUnits());
        dao.setTotal(model.getTotal());
        return dao;
    }
}
