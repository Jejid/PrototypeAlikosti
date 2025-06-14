package com.example.controller;

import com.example.dto.OrderProcessedDto;
import com.example.model.OrderProcessed;
import com.example.service.OrderProcessedService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/orderprocessed")
public class OrderProcessedController {

    private final OrderProcessedService orderProcessedService;

    public OrderProcessedController(OrderProcessedService orderProcessedService) {
        this.orderProcessedService = orderProcessedService;
    }

    @GetMapping
    public ResponseEntity<List<OrderProcessedDto>> getAllOrderProcessed() {
        List<OrderProcessed> orders = orderProcessedService.getAllOrderProcessed();
        List<OrderProcessedDto> dtos = new ArrayList<>();
        for (OrderProcessed order : orders) {
            dtos.add(toDto(order));
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/payment/{paymentId}/product/{productId}")
    public ResponseEntity<OrderProcessedDto> getOrderProcessedById(@PathVariable int paymentId, @PathVariable int productId) {
        OrderProcessed order = orderProcessedService.getOrderProcessedById(paymentId, productId);
        return new ResponseEntity<>(toDto(order), HttpStatus.OK);
    }

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<List<OrderProcessedDto>> getOrderByPaymentId(@PathVariable int paymentId) {
        List<OrderProcessed> paymentOrders = orderProcessedService.getOrderByPaymentId(paymentId);
        List<OrderProcessedDto> dtos = new ArrayList<>();
        for (OrderProcessed order : paymentOrders) {
            dtos.add(toDto(order));
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createOrderProcessed(@Valid @RequestBody OrderProcessed order) {
        OrderProcessed created = orderProcessedService.createOrderProcessed(order);
        Map<String, String> response = new HashMap<>();
        response.put("message", "OrderProcessed creado exitosamente con paymentId: " + created.getPaymentId() + ", productId: " + created.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/many")
    public ResponseEntity<?> createTotalOrderProcessed(@RequestBody List<OrderProcessed> orders) {
        orderProcessedService.createTotalOrderProcessed(orders);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Orden procesada total creada para paymentId: " + orders.get(0).getPaymentId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/payment/{paymentId}/product/{productId}")
    public ResponseEntity<Map<String, String>> deleteOrderProcessed(@PathVariable int paymentId, @PathVariable int productId) {
        orderProcessedService.deleteOrderProcessed(paymentId, productId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "OrderProcessed eliminado para paymentId: " + paymentId + ", productId: " + productId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/payment/{paymentId}")
    public ResponseEntity<Map<String, String>> deleteOrderByPaymentId(@PathVariable int paymentId) {
        orderProcessedService.deleteOrdersByPaymentId(paymentId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Orden procesada eliminada para paymentId: " + paymentId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/payment/{paymentId}/product/{productId}")
    public ResponseEntity<Map<String, String>> updateOrderProcessed(@PathVariable int paymentId, @PathVariable int productId,
                                                                    @Valid @RequestBody OrderProcessed order) {
        OrderProcessed updated = orderProcessedService.updateOrderProcessed(paymentId, productId, order);
        Map<String, String> response = new HashMap<>();
        response.put("message", "OrderProcessed actualizado para paymentId: " + updated.getPaymentId() + ", productId: " + updated.getProductId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/payment/{paymentId}/product/{productId}")
    public ResponseEntity<Map<String, String>> partialUpdateOrderProcessed(@PathVariable int paymentId, @PathVariable int productId,
                                                                           @RequestBody Map<String, Object> updates) {
        OrderProcessed updated = orderProcessedService.partialUpdateOrderProcessed(paymentId, productId, updates);
        Map<String, String> response = new HashMap<>();
        response.put("message", "OrderProcessed parcialmente actualizado para paymentId: " + updated.getPaymentId() + ", productId: " + updated.getProductId());
        return ResponseEntity.ok(response);
    }

    // de model a dto
    private OrderProcessedDto toDto(OrderProcessed order) {
        OrderProcessedDto dto = new OrderProcessedDto();
        dto.setPaymentId(order.getPaymentId());
        dto.setProductId(order.getProductId());
        dto.setUnits(order.getUnits());
        dto.setTotal_product(order.getTotal_product());
        return dto;
    }
}
