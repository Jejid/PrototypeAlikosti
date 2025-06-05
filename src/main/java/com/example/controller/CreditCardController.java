package com.example.controller;

import com.example.dto.CreditCardDto;
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
    public ResponseEntity<List<CreditCardDto>> getAllCreditCards() {
        List<CreditCard> cards = creditCardService.getAllCreditCards();
        List<CreditCardDto> cardDtos = new ArrayList<>();

        for (CreditCard card : cards) {
            CreditCardDto dto = new CreditCardDto();
            dto.setName(card.getName());
            dto.setBank(card.getBank());
            cardDtos.add(dto);
        }

        return new ResponseEntity<>(cardDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditCardDto> getCreditCardById(@PathVariable Integer id) {
        CreditCard card = creditCardService.getCreditCardById(id);
        CreditCardDto dto = new CreditCardDto();
        dto.setName(card.getName());
        dto.setBank(card.getBank());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createCreditCard(@Valid @RequestBody CreditCard card) {
        CreditCard created = creditCardService.createCreditCard(card);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Tarjeta de crédito de: " + created.getName() + " creada exitosamente");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateCreditCard(@PathVariable Integer id,
                                                                @Valid @RequestBody CreditCard card) {
        CreditCard updated = creditCardService.updateCreditCard(id, card);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Tarjeta de crédito de: " + updated.getName() + " actualizada exitosamente");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> partialUpdateCreditCard(@PathVariable Integer id,
                                                                       @RequestBody Map<String, Object> updates) {
        CreditCard updated = creditCardService.partialUpdateCreditCard(id, updates);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Tarjeta de crédito de: " + updated.getName() + " campo/s actualizado/s exitosamente");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCreditCard(@PathVariable Integer id) {
        String name = creditCardService.getCreditCardById(id).getName();
        creditCardService.deleteCreditCard(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Tarjeta de crédito de: " + name + " eliminada exitosamente");

        return ResponseEntity.ok(response);
    }
}
