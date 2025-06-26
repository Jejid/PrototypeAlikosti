package com.example.controller;

import com.example.dto.RequestRefundDto;
import com.example.mapper.RequestRefundMapper;
import com.example.model.RequestRefund;
import com.example.service.RequestRefundService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/refunds")
public class RequestRefundController {

    private final RequestRefundService requestRefundService;
    private final RequestRefundMapper requestRefundMapper;

    public RequestRefundController(RequestRefundService requestRefundService, RequestRefundMapper requestRefundMapper) {
        this.requestRefundService = requestRefundService;
        this.requestRefundMapper = requestRefundMapper;
    }

    @GetMapping
    public ResponseEntity<List<RequestRefundDto>> getAllRequestRefunds() {
        return new ResponseEntity<>(
                requestRefundService.getAllRequestRefunds().stream()
                        .map(requestRefundMapper::toPublicDto)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestRefundDto> getRequestRefundById(@PathVariable Integer id) {
        return new ResponseEntity<>(
                requestRefundMapper.toPublicDto(requestRefundService.getRequestRefundById(id)),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createRequestRefund(@Valid @RequestBody RequestRefundDto requestRefundDto) {
        RequestRefund created = requestRefundService.createRequestRefund(requestRefundDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Solicitud de reembolso con ID: " + created.getId() + ", creada exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteRequestRefund(@PathVariable Integer id) {
        requestRefundService.deleteRequestRefund(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Solicitud de reembolso con ID: " + id + ", fue eliminada exitosamente");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateRequestRefund(@PathVariable Integer id, @Valid @RequestBody RequestRefundDto requestRefundDto) {
        RequestRefund updated = requestRefundService.updateRequestRefund(id, requestRefundDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Solicitud de reembolso con ID: " + id + ", actualizada exitosamente");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> partialUpdateRequestRefund(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        RequestRefund updated = requestRefundService.partialUpdateRequestRefund(id, updates);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Solicitud de reembolso con ID: " + updated.getId() + ", campo/s actualizado/s exitosamente");
        return ResponseEntity.ok(response);
    }
}
