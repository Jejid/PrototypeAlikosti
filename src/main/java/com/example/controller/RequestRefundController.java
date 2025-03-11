package com.example.controller;

import com.example.exception.EntityNotFoundException;
import com.example.model.RequestRefund;
import com.example.service.RequestRefundService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/request-refunds")
public class RequestRefundController {

    private static final Logger logger = LoggerFactory.getLogger(RequestRefundController.class);
    private final RequestRefundService requestRefundService;

    public RequestRefundController(RequestRefundService requestRefundService) {
        this.requestRefundService = requestRefundService;
    }

    @GetMapping
    public List<RequestRefund> getAllRequestRefunds() {
        return requestRefundService.getAllRequestRefunds();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestRefund> getRequestRefundById(@PathVariable Integer id) {
        return requestRefundService.getRequestRefundById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud de reembolso con ID " + id + " no encontrada"));
    }

    @PostMapping
    public ResponseEntity<RequestRefund> createRequestRefund(@Valid @RequestBody RequestRefund requestRefund) {
        RequestRefund savedRequestRefund = requestRefundService.createRequestRefund(requestRefund);
        return ResponseEntity.ok(savedRequestRefund);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequestRefund> updateRequestRefund(@PathVariable Integer id, @Valid @RequestBody RequestRefund requestRefund) {
        logger.info("Intentando actualizar la solicitud de reembolso con ID: {}", id);
        RequestRefund updatedRequestRefund = requestRefundService.updateRequestRefund(id, requestRefund);
        logger.info("Solicitud de reembolso actualizada correctamente: {}", updatedRequestRefund);
        return ResponseEntity.ok(updatedRequestRefund);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RequestRefund> partialUpdateRequestRefund(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> updates) {

        RequestRefund requestRefund = requestRefundService.getRequestRefundById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud de reembolso con ID " + id + " no encontrada"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "buyerId":
                    if (value instanceof Number) requestRefund.setBuyerId(((Number) value).intValue());
                    break;
                case "paymentId":
                    if (value instanceof Number) requestRefund.setPaymentId(((Number) value).intValue());
                    break;
                case "confirmation":
                    if (value instanceof Number) requestRefund.setConfirmation(((Number) value).intValue());
                    break;
                case "refundType":
                    if (value instanceof Number) requestRefund.setRefundType(((Number) value).intValue());
                    break;
                default:
                    throw new IllegalArgumentException("El campo " + key + " no es válido o no existe para actualización.");
            }
        });

        requestRefundService.createRequestRefund(requestRefund);
        return ResponseEntity.ok(requestRefund);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteRequestRefund(@PathVariable Integer id) {
        requestRefundService.getRequestRefundById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud de reembolso con ID " + id + " no encontrada para eliminar"));

        requestRefundService.deleteRequestRefund(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Solicitud de reembolso eliminada exitosamente");

        return ResponseEntity.ok(response);
    }

}
