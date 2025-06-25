package com.example.controller;

import com.example.dto.PaymentMethodDto;
import com.example.model.PaymentMethod;
import com.example.service.PaymentMethodService;
import com.example.utility.PaymentMethodMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;
    private final PaymentMethodMapper paymentMethodMapper;

    public PaymentMethodController(PaymentMethodService paymentMethodService, PaymentMethodMapper paymentMethodMapper) {
        this.paymentMethodService = paymentMethodService;
        this.paymentMethodMapper = paymentMethodMapper;
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodDto>> getAllPaymentMethods() {
        return new ResponseEntity<>(
                paymentMethodService.getAllPaymentMethods().stream()
                        .map(paymentMethodMapper::toPublicDto)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDto> getPaymentMethodById(@PathVariable Integer id) {
        return new ResponseEntity<>(
                paymentMethodMapper.toPublicDto(paymentMethodService.getPaymentMethodById(id)),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createPaymentMethod(@Valid @RequestBody PaymentMethodDto paymentMethodDto) {
        PaymentMethod created = paymentMethodService.createPaymentMethod(paymentMethodDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Método de pago: " + created.getName() + " con ID: " + created.getId() + ", creado exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePaymentMethod(@PathVariable Integer id) {
        String name = paymentMethodService.getPaymentMethodById(id).getName();
        paymentMethodService.deletePaymentMethod(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Método de pago: " + name + " de ID: " + id + ", fue eliminado exitosamente");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updatePaymentMethod(@PathVariable Integer id, @Valid @RequestBody PaymentMethodDto paymentMethodDto) {
        PaymentMethod updated = paymentMethodService.updatePaymentMethod(id, paymentMethodDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Método de pago: " + updated.getName() + " de ID: " + id + ", actualizado exitosamente");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> partialUpdatePaymentMethod(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        PaymentMethod updated = paymentMethodService.partialUpdatePaymentMethod(id, updates);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Método de pago: " + updated.getName() + ", campo/s actualizado/s exitosamente");
        return ResponseEntity.ok(response);
    }
}
