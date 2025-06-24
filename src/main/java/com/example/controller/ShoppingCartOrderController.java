package com.example.controller;

import com.example.dto.ShoppingCartOrderDto;
import com.example.model.ShoppingCartOrder;
import com.example.service.BuyerService;
import com.example.service.ShoppingCartOrderService;
import com.example.utility.ShoppingCartOrderMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cartorders")
public class ShoppingCartOrderController {

    private final ShoppingCartOrderService shoppingCartOrderService;
    private final ShoppingCartOrderMapper itemOrderMapper;
    private final BuyerService buyerService;

    public ShoppingCartOrderController(ShoppingCartOrderService shoppingCartOrderService, ShoppingCartOrderMapper itemOrderMapper, BuyerService buyerService) {
        this.shoppingCartOrderService = shoppingCartOrderService;
        this.itemOrderMapper = itemOrderMapper;
        this.buyerService = buyerService;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingCartOrderDto>> getAllItemsOrders() {
        return new ResponseEntity<>(shoppingCartOrderService.getAllItemOrders().stream().map(itemOrderMapper::toPublicDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/buyer/{buyerId}/product/{productId}")
    public ResponseEntity<ShoppingCartOrderDto> getItemOrderById(@PathVariable int buyerId, @PathVariable int productId) {
        return new ResponseEntity<>(itemOrderMapper.toPublicDto(shoppingCartOrderService.getItemOrderById(buyerId, productId)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createItemOrder(@Valid @RequestBody ShoppingCartOrderDto itemOrderDto) {
        //ShoppingCartOrder created = shoppingCartOrderService.createItemOrder(itemOrderDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Producto de orden exitosamente agregado al carrito del comprador: " + buyerService.getBuyerById(shoppingCartOrderService.createItemOrder(itemOrderDto).getBuyerId()).getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //metodo para obtener todos los productos en la orden del mismo comprador. (el carrito de compras del usuario)
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<ShoppingCartOrderDto>> getOrderByBuyerId(@PathVariable int buyerId) {
        return new ResponseEntity<>(shoppingCartOrderService.getOrderByBuyerId(buyerId).stream().map(itemOrderMapper::toPublicDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping("/many")
    public ResponseEntity<?> createTotalOrder(@RequestBody List<ShoppingCartOrder> orderList) {
        shoppingCartOrderService.createTotalOrder(orderList);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Orden total creada para comprador con ID: " + orderList.get(0).getBuyerId());
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
        shoppingCartOrderService.deleteOrderByBuyerId(buyerId);
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
