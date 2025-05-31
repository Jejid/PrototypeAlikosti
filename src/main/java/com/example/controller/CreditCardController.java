package com.example.controller;

import com.example.model.CreditCard;
import com.example.service.CreditCardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/credit-cards")
public class CreditCardController {

    private final CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @GetMapping
    public ResponseEntity<List<CreditCard>> getAllCreditCards() {
        List<CreditCard> list = creditCardService.getAllCreditCards();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditCard> getCreditCardById(@PathVariable Integer id) {
        CreditCard card = creditCardService.getCreditCardById(id);
        return ResponseEntity.ok(card);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createCreditCard(@Valid @RequestBody CreditCard creditCard) {
        CreditCard created = creditCardService.createCreditCard(creditCard);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Tarjeta de crédito creada exitosamente con ID: " + created.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateCreditCard(@PathVariable Integer id, @Valid @RequestBody CreditCard creditCard) {
        CreditCard updated = creditCardService.updateCreditCard(id, creditCard);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Tarjeta de crédito actualizada exitosamente para el comprador ID: " + updated.getBuyerId());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> partialUpdateCreditCard(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        CreditCard updated = creditCardService.partialUpdateCreditCard(id, updates);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Tarjeta de crédito actualizada parcialmente para el comprador ID: " + updated.getBuyerId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCreditCard(@PathVariable Integer id) {
        creditCardService.deleteCreditCard(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Tarjeta de crédito con ID " + id + " eliminada exitosamente");

        return ResponseEntity.ok(response);
    }
}
