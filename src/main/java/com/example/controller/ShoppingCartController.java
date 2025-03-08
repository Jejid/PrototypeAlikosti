/*package com.example.controller;

import com.example.exception.EntityNotFoundException;
import com.example.model.ShoppingCart;
import com.example.service.ShoppingCartService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shoppincart")
public class ShoppingCartController {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public List<ShoppingCart> getAllShoppingCarts() {
        return shoppingCartService.getAllShoppingCarts();
    }

    @GetMapping("/{idbuyer}")
    public ResponseEntity<ShoppingCart> getShoppingCartByBuyerId(@PathVariable Integer id) {
        return shoppingCartService.getShoppingCartById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("ShoppingCart con ID de comprador " + id + " no encontrado"));
    }

    @PostMapping
    public ResponseEntity<ShoppingCart> createShoppingCart(@Valid @RequestBody ShoppingCart shoppingCart) {
        ShoppingCart savedShoppingCart = shoppingCartService.createShoppingCart(shoppingCart);
        return ResponseEntity.ok(savedShoppingCart);
    }

    @PutMapping("/{idbuyer}")
    public ResponseEntity<ShoppingCart> updateShoppingCart(@PathVariable Integer id,@Valid @RequestBody ShoppingCart shoppingCart) {
        logger.info("Intentando actualizar el shoppingCarto con ID de comprador: {}", id);
        ShoppingCart updatedShoppingCart = shoppingCartService.updateShoppingCart(id, shoppingCart);
        logger.info("ShoppingCart actualizado correctamente: {}", updatedShoppingCart);
        return ResponseEntity.ok(updatedShoppingCart);
    }

    @DeleteMapping("/{idbuyer}")
    public ResponseEntity<Void> deleteShoppingCart(@PathVariable Integer id) {
        shoppingCartService.deleteShoppingCart(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{idbuyer}")
    public ResponseEntity<ShoppingCart> partialUpdateShoppingCart(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> updates) {

        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartById(id)
                .orElseThrow(() -> new EntityNotFoundException("ShoppingCarto con ID " + id + " no encontrado"));

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "productId":
                        if (value instanceof Number) shoppingCart.setProductId(((Number) value).intValue());
                        break;
                    case "units":
                        if (value instanceof Number) shoppingCart.setUnits(((Number) value).intValue());
                        break;
                    case "total":
                        if (value instanceof Number) shoppingCart.setTotal(((Number) value).intValue());
                        break;
                    default:
                        throw new IllegalArgumentException("El campo " + key + " no es válido o no existe para actualización.");
                }
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Tipo de dato incorrecto para el campo " + key);
            }
        });


        shoppingCartService.createShoppingCart(shoppingCart);
        return ResponseEntity.ok(shoppingCart);
    }


}
*/