package com.example.controller;

import com.example.dto.ShoppingCartOrderDto;
import com.example.model.ShoppingCartOrder;
import com.example.service.ShoppingCartOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/cartorders")
public class ShoppingCartOrderController {

    private final ShoppingCartOrderService shoppingCartOrderService;

    public ShoppingCartOrderController(ShoppingCartOrderService shoppingCartOrderService) {
        this.shoppingCartOrderService = shoppingCartOrderService;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingCartOrderDto>> getAllItemsOrders() {
        List<ShoppingCartOrder> orders = shoppingCartOrderService.getAllItemOrders();
        List<ShoppingCartOrderDto> dtos = new ArrayList<>();
        for (ShoppingCartOrder order : orders) {
            dtos.add(toDto(order));
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/buyer/{buyerId}/product/{productId}")
    public ResponseEntity<ShoppingCartOrderDto> getItemOrderById(@PathVariable int buyerId, @PathVariable int productId) {
        ShoppingCartOrder order = shoppingCartOrderService.getItemOrderById(buyerId, productId);
        return new ResponseEntity<>(toDto(order), HttpStatus.OK);
    }

    //metodo para obtener todos los productos en la orden del mismo comprador. (el carrito de compras del usuario)
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<ShoppingCartOrderDto>> getOrderByBuyerId(@PathVariable int buyerId) {
        List<ShoppingCartOrder> userOrder = shoppingCartOrderService.getOrderByBuyerId(buyerId);
        List<ShoppingCartOrderDto> userOrderDto = new ArrayList<>();
        for(ShoppingCartOrder order: userOrder){
            userOrderDto.add(toDto(order));
        }
        return new ResponseEntity<>(userOrderDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createItemOrder(@Valid @RequestBody ShoppingCartOrder order) {
        ShoppingCartOrder created = shoppingCartOrderService.createItemOrder(order);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Item de orden creado exitosamente con buyerId: " + created.getBuyerId() + ", productId: " + created.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/varios")
    public ResponseEntity<?> createTotalOrder(@RequestBody List<ShoppingCartOrder> orderList) {
        shoppingCartOrderService.createTotalOrder(orderList);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Orden total creada para comprador con ID: " + orderList.get(0).getBuyerId()) ;
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @DeleteMapping("/buyer/{buyerId}/product/{productId}")
    public ResponseEntity<Map<String, String>> deleteItemOrder(@PathVariable int buyerId, @PathVariable int productId) {
        shoppingCartOrderService.deleteItemOrder(buyerId, productId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Item de orden eliminado para buyerId: " + buyerId + ", productId: " + productId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/buyer/{buyerId}")
    public ResponseEntity<Map<String, String>> deleteOrderByBuyerId(@PathVariable int buyerId) {
        shoppingCartOrderService.deleteOrderByBuyer(buyerId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Orden / carrito eliminado para el comprador con ID: " + buyerId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/buyer/{buyerId}/product/{productId}")
    public ResponseEntity<Map<String, String>> updateItemOrder(@PathVariable int buyerId, @PathVariable int productId,
                                                               @Valid @RequestBody ShoppingCartOrder order) {
        ShoppingCartOrder updated = shoppingCartOrderService.updateItemOrder(buyerId, productId, order);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Item de orden actualizado para buyerId: " + updated.getBuyerId() + ", productId: " + updated.getProductId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/buyer/{buyerId}/product/{productId}")
    public ResponseEntity<Map<String, String>> partialUpdateItemOrder(@PathVariable int buyerId, @PathVariable int productId,
                                                                      @RequestBody Map<String, Object> updates) {
        ShoppingCartOrder updated = shoppingCartOrderService.partialUpdateItemOrder(buyerId, productId, updates);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Item de orden parcialmente actualizado para buyerId: " + updated.getBuyerId() + ", productId: " + updated.getProductId());
        return ResponseEntity.ok(response);
    }

    // metodo auxiliar para mapear modelo a DTO
    private ShoppingCartOrderDto toDto(ShoppingCartOrder order) {
        ShoppingCartOrderDto dto = new ShoppingCartOrderDto();
        dto.setBuyerId(order.getBuyerId());
        dto.setProductId(order.getProductId());
        dto.setUnits(order.getUnits());
        dto.setTotal_product(order.getTotal_product());
        return dto;
    }
}
