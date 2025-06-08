package com.example.service;

import com.example.dao.OrderProcessedDao;
import com.example.key.OrderProcessedKey;
import com.example.model.OrderProcessed;
import com.example.repository.OrderProcessedRepository;
import com.example.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderProcessedService {

    private final OrderProcessedRepository repository;

    public OrderProcessedService(OrderProcessedRepository repository) {
        this.repository = repository;
    }

    public List<OrderProcessed> getAllOrderProcessed() {
        List<OrderProcessedDao> daoList = repository.findAll();
        return daoList.stream().map(this::toModel).collect(Collectors.toList());
    }

    public OrderProcessed getOrderProcessedById(int paymentId, int productId) {
        OrderProcessedDao dao = repository.findById(new OrderProcessedKey(paymentId, productId))
                .orElseThrow(() -> new EntityNotFoundException("Orden procesada con paymentId: " + paymentId + " y productId: " + productId + " no existe"));
        return toModel(dao);
    }

    public List<OrderProcessed> getOrderByPaymentId(int paymentId) {
        List<OrderProcessedDao> daoList = repository.findAll();

        List<OrderProcessed> ordersByPayment = new ArrayList<>();
        for (OrderProcessedDao dao : daoList) {
            if (dao.getPaymentId() == paymentId) ordersByPayment.add(toModel(dao));
        }
        if (ordersByPayment.isEmpty()) throw new EntityNotFoundException("No existen órdenes procesadas con paymentId: " + paymentId);
        return ordersByPayment;
    }

    public OrderProcessed createOrderProcessed(OrderProcessed orderProcessed) {
        OrderProcessedKey key = new OrderProcessedKey(orderProcessed.getPaymentId(), orderProcessed.getProductId());

        if (repository.existsById(key))
            throw new IllegalArgumentException("La orden procesada con paymentId " + orderProcessed.getPaymentId() + " y productId " + orderProcessed.getProductId() + " ya existe.");

        OrderProcessedDao dao = toDao(orderProcessed);
        OrderProcessedDao saved = repository.save(dao);
        return toModel(saved);
    }

    public void createTotalOrderProcessed(List<OrderProcessed> ordersProcessed) {
        if (ordersProcessed.isEmpty()) throw new IllegalArgumentException("La lista de órdenes procesadas está vacía.");

        for (OrderProcessed orderProcessed : ordersProcessed) {
            OrderProcessedDao dao = toDao(orderProcessed);
            repository.save(dao);
        }
    }

    public OrderProcessed updateOrderProcessed(int paymentId, int productId, OrderProcessed orderProcessed) {
        OrderProcessedKey key = new OrderProcessedKey(paymentId, productId);
        OrderProcessedDao existingDao = repository.findById(key)
                .orElseThrow(() -> new EntityNotFoundException("Orden procesada con paymentId: " + paymentId + " y productId: " + productId + " no existe"));

        existingDao.setUnits(orderProcessed.getUnits());
        existingDao.setTotal_product(orderProcessed.getTotal_product());

        OrderProcessedDao saved = repository.save(existingDao);
        return toModel(saved);
    }

    public void deleteOrderProcessed(int paymentId, int productId) {
        OrderProcessedKey key = new OrderProcessedKey(paymentId, productId);
        if (!repository.existsById(key)) {
            throw new EntityNotFoundException("Orden procesada con paymentId: " + paymentId + " y productId: " + productId + " no existe");
        }
        repository.deleteById(key);
    }

    public void deleteOrdersByPaymentId(int paymentId) {
        List<OrderProcessed> ordersByPayment = getOrderByPaymentId(paymentId);
        for (OrderProcessed orderProcessed : ordersByPayment) {
            OrderProcessedKey key = new OrderProcessedKey(orderProcessed.getPaymentId(), orderProcessed.getProductId());
            repository.deleteById(key);
        }
    }

    public OrderProcessed partialUpdateOrderProcessed(int paymentId, int productId, Map<String, Object> updates) {
        OrderProcessedKey key = new OrderProcessedKey(paymentId, productId);
        OrderProcessedDao existingDao = repository.findById(key)
                .orElseThrow(() -> new EntityNotFoundException("Orden procesada con paymentId: " + paymentId + " y productId: " + productId + " no existe"));

        if (updates.containsKey("units")) {
            existingDao.setUnits((Integer) updates.get("units"));
        }
        if (updates.containsKey("total_product")) {
            existingDao.setTotal_product((Integer) updates.get("total_product"));
        }

        OrderProcessedDao saved = repository.save(existingDao);
        return toModel(saved);
    }

    // Conversion DAO <-> Model
    private OrderProcessed toModel(OrderProcessedDao dao) {
        OrderProcessed model = new OrderProcessed();
        model.setPaymentId(dao.getPaymentId());
        model.setProductId(dao.getProductId());
        model.setUnits(dao.getUnits());
        model.setTotal_product(dao.getTotal_product());
        return model;
    }

    private OrderProcessedDao toDao(OrderProcessed model) {
        OrderProcessedDao dao = new OrderProcessedDao();
        dao.setPaymentId(model.getPaymentId());
        dao.setProductId(model.getProductId());
        dao.setUnits(model.getUnits());
        dao.setTotal_product(model.getTotal_product());
        return dao;
    }
}
