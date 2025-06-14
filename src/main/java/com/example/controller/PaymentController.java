package com.example.controller;

import com.example.dto.PaymentDto;
import com.example.model.Payment;
import com.example.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        List<PaymentDto> paymentDtoList = new ArrayList<>();
        List<Payment> paymentList = paymentService.getAllPayments();

        for (Payment payment : paymentList) {
            PaymentDto dto = new PaymentDto();
            dto.setBuyerId(payment.getBuyerId());
            dto.setPaymentMethodId(payment.getPaymentMethodId());
            dto.setTotalOrder(payment.getTotalOrder());
            dto.setDate(payment.getDate());
            dto.setConfirmation(payment.getConfirmation());
            dto.setCodeConfirmation(payment.getCodeConfirmation());
            dto.setCardNumber(payment.getCardNumber());
            dto.setRefunded(payment.isRefunded());
            paymentDtoList.add(dto);
        }

        return new ResponseEntity<>(paymentDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Integer id) {
        Payment payment = paymentService.getPaymentById(id);
        PaymentDto dto = new PaymentDto();
        dto.setBuyerId(payment.getBuyerId());
        dto.setPaymentMethodId(payment.getPaymentMethodId());
        dto.setTotalOrder(payment.getTotalOrder());
        dto.setDate(payment.getDate());
        dto.setConfirmation(payment.getConfirmation());
        dto.setCodeConfirmation(payment.getCodeConfirmation());
        dto.setCardNumber(payment.getCardNumber());
        dto.setRefunded(payment.isRefunded());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createPayment(@Valid @RequestBody Payment payment) {
        Payment created = paymentService.createPayment(payment);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Pago creado exitosamente con ID: " + created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePayment(@PathVariable Integer id) {
        paymentService.deletePayment(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Pago con ID: " + id + " eliminado exitosamente");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updatePayment(@PathVariable Integer id, @Valid @RequestBody Payment payment) {
        Payment updated = paymentService.updatePayment(id, payment);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Pago con ID: " + updated.getId() + ", actualizado exitosamente");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> partialUpdatePayment(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        Payment updated = paymentService.partialUpdatePayment(id, updates);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Pago con ID: " + updated.getId() + ", campo/s actualizado/s exitosamente");
        return ResponseEntity.ok(response);
    }
}
