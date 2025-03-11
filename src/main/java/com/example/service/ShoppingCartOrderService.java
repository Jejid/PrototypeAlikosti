package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.ShoppingCartOrder;
import com.example.model.ShoppingCartOrderId;
import com.example.repository.ShoppingCartOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartOrderService {
    private final ShoppingCartOrderRepository shoppingCartOrderRepository;

    public ShoppingCartOrderService(ShoppingCartOrderRepository shoppingCartOrderRepository) {
        this.shoppingCartOrderRepository = shoppingCartOrderRepository;
    }

    public List<ShoppingCartOrder> getAllShoppingCartOrders() {
        return shoppingCartOrderRepository.findAll();
    }

    public ShoppingCartOrder getShoppingCartOrderById(ShoppingCartOrderId id) {
        return shoppingCartOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ShoppingCartOrder no encontrado"));
    }

    public ShoppingCartOrder createShoppingCartOrder(ShoppingCartOrder shoppingCartOrder) {
        return shoppingCartOrderRepository.save(shoppingCartOrder);
    }

    public ShoppingCartOrder updateShoppingCartOrder(ShoppingCartOrderId id, ShoppingCartOrder updatedOrder) {
        ShoppingCartOrder order = getShoppingCartOrderById(id);
        order.setUnits(updatedOrder.getUnits());
        order.setTotal(updatedOrder.getTotal());
        return shoppingCartOrderRepository.save(order);
    }

    public void deleteShoppingCartOrder(ShoppingCartOrderId id) {
        ShoppingCartOrder order = getShoppingCartOrderById(id);
        shoppingCartOrderRepository.delete(order);
    }
}
