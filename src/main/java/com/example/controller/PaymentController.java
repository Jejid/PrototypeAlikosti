package com.example.controller;

import com.example.exception.EntityNotFoundException;
import com.example.model.Payment;
import com.example.service.PaymentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Integer id) {
        return paymentService.getPaymentById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Pago con ID " + id + " no encontrado"));
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) {
        Payment savedPayment = paymentService.createPayment(payment);
        return ResponseEntity.ok(savedPayment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Integer id, @Valid @RequestBody Payment payment) {
        logger.info("Intentando actualizar el pago con ID: {}", id);
        Payment updatedPayment = paymentService.updatePayment(id, payment);
        logger.info("Pago actualizado correctamente: {}", updatedPayment);
        return ResponseEntity.ok(updatedPayment);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Payment> partialUpdatePayment(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> updates) {

        Payment payment = paymentService.getPaymentById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago con ID " + id + " no encontrado"));

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "buyerId":
                        if (value instanceof Number) payment.setBuyerId(((Number) value).intValue());
                        break;
                    case "paymentMethodId":
                        if (value instanceof Number) payment.setPaymentMethodId(((Number) value).intValue());
                        break;
                    case "totalOrder":
                        if (value instanceof Number) payment.setTotalOrder(((Number) value).intValue());
                        break;
                    case "date":
                        if (value instanceof String) payment.setDate((String) value);
                        break;
                    case "confirmation":
                        if (value instanceof Number) payment.setConfirmation(((Number) value).intValue());
                        break;
                    case "codeConfirmation":
                        if (value instanceof Number) payment.setCodeConfirmation(((Number) value).intValue());
                        break;
                    case "cardNumber":
                        if (value instanceof String) payment.setCardNumber((String) value);
                        break;
                    case "refunded":
                        if (value instanceof Boolean) payment.setRefunded((Boolean) value);
                        break;
                    default:
                        throw new IllegalArgumentException("El campo " + key + " no es válido o no existe para actualización.");
                }
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Tipo de dato incorrecto para el campo " + key);
            }
        });

        paymentService.createPayment(payment);
        return ResponseEntity.ok(payment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>>  deletePayment(@PathVariable Integer id) {
        paymentService.getPaymentById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago con ID " + id + " no encontrado para eliminar"));
        paymentService.deletePayment(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Pago eliminado exitosamente");

        return ResponseEntity.ok(response);
    }
}