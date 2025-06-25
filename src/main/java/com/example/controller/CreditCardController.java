package com.example.controller;

import com.example.dto.CreditCardDto;
import com.example.model.CreditCard;
import com.example.service.CreditCardService;
import com.example.utility.CreditCardMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/credit-cards")
public class CreditCardController {

    private final CreditCardService creditCardService;
    private final CreditCardMapper creditCardMapper;

    public CreditCardController(CreditCardService creditCardService, CreditCardMapper creditCardMapper) {
        this.creditCardService = creditCardService;
        this.creditCardMapper = creditCardMapper;
    }

    @GetMapping
    public ResponseEntity<List<CreditCardDto>> getAllCreditCards() {
        return new ResponseEntity<>(
                creditCardService.getAllCreditCards().stream()
                        .map(creditCardMapper::toPublicDto)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditCardDto> getCreditCardById(@PathVariable Integer id) {
        return new ResponseEntity<>(
                creditCardMapper.toPublicDto(creditCardService.getCreditCardById(id)),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createCreditCard(@Valid @RequestBody CreditCardDto creditCardDto) {
        CreditCard created = creditCardService.createCreditCard(creditCardDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Tarjeta: " + created.getName() + " con ID: " + created.getId() + ", creada exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCreditCard(@PathVariable Integer id) {
        String name = creditCardService.getCreditCardById(id).getName();
        creditCardService.deleteCreditCard(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Tarjeta: " + name + " de ID: " + id + ", fue eliminada exitosamente");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateCreditCard(@PathVariable Integer id, @Valid @RequestBody CreditCardDto creditCardDto) {
        CreditCard updated = creditCardService.updateCreditCard(id, creditCardDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Tarjeta: " + updated.getName() + " de ID: " + id + ", actualizada exitosamente");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> partialUpdateCreditCard(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        CreditCard updated = creditCardService.partialUpdateCreditCard(id, updates);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Tarjeta: " + updated.getName() + ", campo/s actualizado/s exitosamente");
        return ResponseEntity.ok(response);
    }
}
