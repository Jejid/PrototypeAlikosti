package com.example.controller;

import com.example.dto.PaymentDto;
import com.example.mapper.PaymentMapper;
import com.example.model.Payment;
import com.example.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    public PaymentController(PaymentService paymentService, PaymentMapper paymentMapper) {
        this.paymentService = paymentService;
        this.paymentMapper = paymentMapper;
    }

    @GetMapping("/buyer/{id}")
    public ResponseEntity<List<PaymentDto>> getPaymentsByBuyerId(@PathVariable Integer id) {
        return new ResponseEntity<>(
                paymentService.getPaymentByBuyerId(id).stream()
                        .map(paymentMapper::toPublicDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Map<String, String>> confirmPayment(@PathVariable Integer id, @RequestParam Integer state) {
        String confirmation = paymentService.confirmPaymenteById(id, state);

        Map<String, String> response = new HashMap<>();
        response.put("message", "El pago con id " + id + " fue: " + confirmation);
        return ResponseEntity.ok(response);
    }

    //----- Métodos básicos-------
    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        return new ResponseEntity<>(
                paymentService.getAllPayments().stream()
                        .map(paymentMapper::toPublicDto)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Integer id) {
        return new ResponseEntity<>(
                paymentMapper.toPublicDto(paymentService.getPaymentById(id)),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createPayment(@Valid @RequestBody PaymentDto paymentDto) {
        Payment created = paymentService.createPayment(paymentDto);


        Map<String, String> response = new HashMap<>();
        response.put("message", "Pago con ID: " + created.getId() + ", creado exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePayment(@PathVariable Integer id) {
        paymentService.deletePayment(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Pago con ID: " + id + ", fue eliminado exitosamente");
        return ResponseEntity.ok(response);
    }

    /*@PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updatePayment(@PathVariable Integer id, @Valid @RequestBody PaymentDto paymentDto) {
        Payment updated = paymentService.updatePayment(id, paymentDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Pago con ID: " + id + ", actualizado exitosamente");
        return ResponseEntity.ok(response);
    }*/

    /*
    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> partialUpdatePayment(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        Payment updated = paymentService.partialUpdatePayment(id, updates);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Pago con ID: " + updated.getId() + ", campo/s actualizado/s exitosamente");
        return ResponseEntity.ok(response);
    }
    */
}
