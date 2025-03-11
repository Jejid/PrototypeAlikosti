package com.example.controller;

import com.example.model.ShoppingCartOrder;
import com.example.model.ShoppingCartOrderId;
import com.example.service.ShoppingCartOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shoppingcart")
public class ShoppingCartOrderController {
    private final ShoppingCartOrderService shoppingCartOrderService;

    public ShoppingCartOrderController(ShoppingCartOrderService shoppingCartOrderService) {
        this.shoppingCartOrderService = shoppingCartOrderService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllShoppingCartOrders() {
        List<ShoppingCartOrder> orders = shoppingCartOrderService.getAllShoppingCartOrders();

        List<Map<String, Object>> response = orders.stream().map(order -> {
            Map<String, Object> map = new HashMap<>();
            map.put("buyerId", order.getBuyer().getId());
            map.put("productId", order.getProduct().getId());
            map.put("units", order.getUnits());
            map.put("total", order.getTotal());
            return map;
        }).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{buyerId}/{productId}")
    public ResponseEntity<Map<String, Object>> getShoppingCartOrderById(
            @PathVariable Integer buyerId,
            @PathVariable Integer productId) {

        ShoppingCartOrderId id = new ShoppingCartOrderId(buyerId, productId);
        ShoppingCartOrder order = shoppingCartOrderService.getShoppingCartOrderById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("buyerId", order.getBuyer().getId());
        response.put("productId", order.getProduct().getId());
        response.put("units", order.getUnits());
        response.put("total", order.getTotal());

        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<Map<String, Object>> createShoppingCartOrder(@RequestBody ShoppingCartOrder order) {
        ShoppingCartOrder savedOrder = shoppingCartOrderService.createShoppingCartOrder(order);

        Map<String, Object> response = new HashMap<>();
        response.put("buyerId", savedOrder.getBuyer().getId());
        response.put("productId", savedOrder.getProduct().getId());
        response.put("units", savedOrder.getUnits());
        response.put("total", savedOrder.getTotal());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping("/{buyerId}/{productId}")
    public ResponseEntity<Map<String, Object>> updateShoppingCartOrder(
            @PathVariable Integer buyerId,
            @PathVariable Integer productId,
            @RequestBody ShoppingCartOrder updatedOrder) {

        ShoppingCartOrderId id = new ShoppingCartOrderId(buyerId, productId);
        ShoppingCartOrder order = shoppingCartOrderService.updateShoppingCartOrder(id, updatedOrder);

        Map<String, Object> response = new HashMap<>();
        response.put("buyerId", order.getBuyer().getId());
        response.put("productId", order.getProduct().getId());
        response.put("units", order.getUnits());
        response.put("total", order.getTotal());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{buyerId}/{productId}")
    public ResponseEntity<Map<String, String>> deleteShoppingCartOrder(
            @PathVariable Integer buyerId,
            @PathVariable Integer productId) {

        ShoppingCartOrderId id = new ShoppingCartOrderId(buyerId, productId);
        shoppingCartOrderService.deleteShoppingCartOrder(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Producto de comprador del carrito eliminado exitosamente");

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{buyerId}/{productId}")
    public ResponseEntity<Map<String, Object>> partialUpdateShoppingCartOrder(
            @PathVariable Integer buyerId,
            @PathVariable Integer productId,
            @RequestBody Map<String, Object> updates) {

        ShoppingCartOrderId id = new ShoppingCartOrderId(buyerId, productId);
        ShoppingCartOrder order = shoppingCartOrderService.getShoppingCartOrderById(id);

        updates.forEach((key, value) -> {
            switch (key) {
                case "units":
                    if (value instanceof Number) order.setUnits(((Number) value).intValue());
                    break;
                case "total":
                    if (value instanceof Number) order.setTotal(((Number) value).intValue());
                    break;
                default:
                    throw new IllegalArgumentException("Campo no válido para actualización: " + key);
            }
        });

        ShoppingCartOrder updatedOrder = shoppingCartOrderService.createShoppingCartOrder(order);

        // Construir la respuesta con solo los campos relevantes
        Map<String, Object> response = new HashMap<>();
        response.put("buyerId", updatedOrder.getBuyer().getId());
        response.put("productId", updatedOrder.getProduct().getId());
        response.put("units", updatedOrder.getUnits());
        response.put("total", updatedOrder.getTotal());

        return ResponseEntity.ok(response);
    }

}
