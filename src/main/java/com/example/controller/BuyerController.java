package com.example.controller;

import com.example.model.Buyer;
import com.example.service.BuyerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/buyers")
public class BuyerController {
    private final BuyerService buyerService;

    public BuyerController(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    @GetMapping
    public List<Buyer> getAllBuyers() {
        return buyerService.getAllBuyers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Buyer> getBuyerById(@PathVariable Integer id) {
        return ResponseEntity.ok(buyerService.getBuyerById(id));
    }

    @PostMapping
    public ResponseEntity<Buyer> createBuyer(@Valid @RequestBody Buyer buyer) {
        return ResponseEntity.ok(buyerService.createBuyer(buyer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Buyer> updateBuyer(@PathVariable Integer id, @Valid @RequestBody Buyer buyer) {
        return ResponseEntity.ok(buyerService.updateBuyer(id, buyer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBuyer(@PathVariable Integer id) {
        buyerService.deleteBuyer(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Comprador eliminado exitosamente");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Buyer> partialUpdateBuyer(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> updates) {
        Buyer buyer = buyerService.getBuyerById(id);

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    if (value instanceof String) buyer.setName((String) value);
                    break;
                case "surname":
                    if (value instanceof String) buyer.setSurname((String) value);
                    break;
                case "birthDate":
                    if (value instanceof String) buyer.setBirthDate((String) value);
                    break;
                case "cc":
                    if (value instanceof String) buyer.setCc((String) value);
                    break;
                case "email":
                    if (value instanceof String) buyer.setEmail((String) value);
                    break;
                case "passAccount":
                    if (value instanceof String) buyer.setPassAccount((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("Campo no válido para actualización: " + key);
            }
        });
        return ResponseEntity.ok(buyerService.createBuyer(buyer));
    }
}
