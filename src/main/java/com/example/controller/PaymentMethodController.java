package com.example.controller;

import com.example.dto.PaymentMethodDto;
import com.example.model.PaymentMethod;
import com.example.service.PaymentMethodService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodDto>> getAllPaymentMethods() {
        List<PaymentMethod> methods = paymentMethodService.getAllPaymentMethods();
        List<PaymentMethodDto> dtos = new ArrayList<>();
        for (PaymentMethod method : methods) {
            PaymentMethodDto dto = new PaymentMethodDto();
            dto.setName(method.getName());
            dto.setDescription(method.getDescription());
            dtos.add(dto);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDto> getPaymentMethodById(@PathVariable Integer id) {
        PaymentMethod method = paymentMethodService.getPaymentMethodById(id);
        PaymentMethodDto dto = new PaymentMethodDto();
        dto.setName(method.getName());
        dto.setDescription(method.getDescription());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createPaymentMethod(@Valid @RequestBody PaymentMethod method) {
        PaymentMethod created = paymentMethodService.createPaymentMethod(method);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Método de pago '" + created.getName() + "' creado exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePaymentMethod(@PathVariable Integer id) {
        String name = paymentMethodService.getPaymentMethodById(id).getName();
        paymentMethodService.deletePaymentMethod(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Método de pago '" + name + "' eliminado exitosamente");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updatePaymentMethod(@PathVariable Integer id, @Valid @RequestBody PaymentMethod method) {
        PaymentMethod updated = paymentMethodService.updatePaymentMethod(id, method);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Método de pago '" + updated.getName() + "' actualizado exitosamente");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> partialUpdatePaymentMethod(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        PaymentMethod updated = paymentMethodService.partialUpdatePaymentMethod(id, updates);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Método de pago '" + updated.getName() + "' actualizado parcialmente");
        return ResponseEntity.ok(response);
    }
}
