package com.example.controller;

import com.example.dto.OrderProcessedDto;
import com.example.dto.ReportSalesDto;
import com.example.mapper.OrderProcessedMapper;
import com.example.model.OrderProcessed;
import com.example.service.OrderProcessedService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orderprocessed")
public class OrderProcessedController {

    private final OrderProcessedService orderProcessedService;
    private final OrderProcessedMapper orderProcessedMapper;

    public OrderProcessedController(OrderProcessedService orderProcessedService, OrderProcessedMapper orderProcessedMapper) {
        this.orderProcessedService = orderProcessedService;
        this.orderProcessedMapper = orderProcessedMapper;
    }

    @GetMapping("totalsales/{buyerId}")
    public Integer getTotalSalesByBuyerId(@PathVariable int buyerId) {
        return orderProcessedService.getTotalSalesByBuyerId(buyerId);
    }

    @GetMapping("/liquid-sales")
    public ResponseEntity<Map<String, Object>> getLiquidSales() {

        List<ReportSalesDto> reportSalesDtos = orderProcessedService.getLuquidSales();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "El total líquido de ventas es: " + reportSalesDtos.stream().mapToInt(ReportSalesDto::getTotalSales).sum());
        response.put("data", reportSalesDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{buyerId}")
    public ResponseEntity<List<OrderProcessedDto>> getSalesByBuyerId(@PathVariable int buyerId) {
        return new ResponseEntity<>(
                orderProcessedService.getSalesByBuyerId(buyerId).stream()
                        .map(orderProcessedMapper::toDto).toList(),
                HttpStatus.OK);
    }


    // ----- Métodos Básicos ----
    @GetMapping
    public ResponseEntity<List<OrderProcessedDto>> getAllOrdersProcessed() {
        return new ResponseEntity<>(
                orderProcessedService.getAllOrdersProcessed().stream()
                        .map(orderProcessedMapper::toDto)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/payment/{paymentId}/product/{productId}")
    public ResponseEntity<OrderProcessedDto> getOrderProcessedById(@PathVariable int paymentId, @PathVariable int productId) {
        return new ResponseEntity<>(
                orderProcessedMapper.toDto(orderProcessedService.getOrderProcessedById(paymentId, productId)),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createOrderProcessed(@Valid @RequestBody OrderProcessedDto dto) {
        OrderProcessed created = orderProcessedService.createItemOrderProcessed(dto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Orden procesada exitosamente para el pago con ID: " + created.getPaymentId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/many")
    public ResponseEntity<Map<String, String>> createMultipleOrdersProcessed(@RequestBody List<OrderProcessedDto> dtoList) {
        orderProcessedService.createMultipleOrdersProcessed(dtoList);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Órdenes procesadas exitosamente para el pago con ID: " + dtoList.get(0).getPaymentId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<List<OrderProcessedDto>> getOrdersByPaymentId(@PathVariable int paymentId) {
        return new ResponseEntity<>(
                orderProcessedService.getOrdersByPaymentId(paymentId).stream()
                        .map(orderProcessedMapper::toDto)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @DeleteMapping("/payment/{paymentId}/product/{productId}")
    public ResponseEntity<Map<String, String>> deleteOrderProcessed(@PathVariable int paymentId, @PathVariable int productId) {
        orderProcessedService.deleteOrderProcessed(paymentId, productId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Orden procesada eliminada para paymentId: " + paymentId + ", productId: " + productId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/payment/{paymentId}")
    public ResponseEntity<Map<String, String>> deleteOrdersByPaymentId(@PathVariable int paymentId) {
        orderProcessedService.deleteOrdersByPaymentId(paymentId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Órdenes procesadas eliminadas para el pago con ID: " + paymentId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/payment/{paymentId}/product/{productId}")
    public ResponseEntity<Map<String, Object>> updateOrderProcessed(@PathVariable int paymentId, @PathVariable int productId,
                                                                    @Valid @RequestBody OrderProcessedDto dto) {
        OrderProcessed updated = orderProcessedService.updateOrderProcessed(paymentId, productId, dto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Orden procesada actualizada para paymentId: " + paymentId + ", productId: " + productId);
        response.put("data", orderProcessedMapper.toDto(updated));
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/payment/{paymentId}/product/{productId}")
    public ResponseEntity<Map<String, Object>> partialUpdateOrderProcessed(@PathVariable int paymentId, @PathVariable int productId,
                                                                           @RequestBody Map<String, Object> updates) {
        OrderProcessed updated = orderProcessedService.partialUpdateOrderProcessed(paymentId, productId, updates);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Orden procesada parcialmente actualizada para paymentId: " + paymentId + ", productId: " + productId);
        response.put("data", orderProcessedMapper.toDto(updated));
        return ResponseEntity.ok(response);
    }
}
