package com.example.controller;

import com.example.dto.RequestRefundDto;
import com.example.model.RequestRefund;
import com.example.service.RequestRefundService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/refunds")
public class RequestRefundController {
    private static final Logger logger = LoggerFactory.getLogger(RequestRefundController.class);
    private final RequestRefundService requestRefundService;

    public RequestRefundController(RequestRefundService requestRefundService) {
        this.requestRefundService = requestRefundService;
    }

    @GetMapping
    public ResponseEntity<List<RequestRefundDto>> getAllRefunds() {
        List<RequestRefundDto> refundDtos = new ArrayList<>();
        List<RequestRefund> refundList = requestRefundService.getAllRefunds();

        for (RequestRefund refund : refundList) {
            RequestRefundDto dto = new RequestRefundDto();
            //dto.setId(refund.getId());
            dto.setBuyerId(refund.getBuyerId());
            dto.setPaymentId(refund.getPaymentId());
            dto.setConfirmation(refund.getConfirmation());
            dto.setRefundType(refund.getRefundType());
            refundDtos.add(dto);
        }

        return new ResponseEntity<>(refundDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestRefundDto> getRefundById(@PathVariable Integer id) {
        RequestRefund refund = requestRefundService.getRefundById(id);

        RequestRefundDto dto = new RequestRefundDto();
        //dto.setId(refund.getId());
        dto.setBuyerId(refund.getBuyerId());
        dto.setPaymentId(refund.getPaymentId());
        dto.setConfirmation(refund.getConfirmation());
        dto.setRefundType(refund.getRefundType());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createRefund(@Valid @RequestBody RequestRefund refund) {
        RequestRefund created = requestRefundService.createRefund(refund);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Reembolso con ID: " + created.getId() + " creado exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteRefund(@PathVariable Integer id) {
        requestRefundService.deleteRefund(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Reembolso con ID " + id + " eliminado exitosamente");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateRefund(@PathVariable Integer id, @Valid @RequestBody RequestRefund refund) {
        RequestRefund updated = requestRefundService.updateRefund(id, refund);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Reembolso con ID " + updated.getId() + " actualizado exitosamente");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> partialUpdateRefund(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        RequestRefund updated = requestRefundService.partialUpdateRefund(id, updates);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Reembolso con ID " + updated.getId() + " actualizado parcialmente con éxito");
        return ResponseEntity.ok(response);
    }
}
