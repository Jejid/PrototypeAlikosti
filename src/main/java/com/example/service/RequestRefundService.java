package com.example.service;

import com.example.model.RequestRefund;
import com.example.repository.RequestRefundRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestRefundService {

    private final RequestRefundRepository requestRefundRepository;

    public RequestRefundService(RequestRefundRepository requestRefundRepository) {
        this.requestRefundRepository = requestRefundRepository;
    }

    public List<RequestRefund> getAllRequestRefunds() {
        return requestRefundRepository.findAll();
    }

    public Optional<RequestRefund> getRequestRefundById(Integer id) {
        return requestRefundRepository.findById(id);
    }

    public RequestRefund createRequestRefund(RequestRefund requestRefund) {
        return requestRefundRepository.save(requestRefund);
    }

    public RequestRefund updateRequestRefund(Integer id, RequestRefund updatedRequestRefund) {
        return requestRefundRepository.findById(id)
                .map(requestRefund -> {
                    requestRefund.setBuyerId(updatedRequestRefund.getBuyerId());
                    requestRefund.setPaymentId(updatedRequestRefund.getPaymentId());
                    requestRefund.setConfirmation(updatedRequestRefund.getConfirmation());
                    requestRefund.setRefundType(updatedRequestRefund.getRefundType());
                    return requestRefundRepository.save(requestRefund);
                })
                .orElseThrow(() -> new RuntimeException("Solicitud de reembolso no encontrada"));
    }

    public void deleteRequestRefund(Integer id) {
        requestRefundRepository.deleteById(id);
    }
}
