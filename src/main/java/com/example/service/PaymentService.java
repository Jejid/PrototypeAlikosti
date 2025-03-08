package com.example.service;

import com.example.model.Payment;
import com.example.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Integer id) {
        return paymentRepository.findById(id);
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment updatePayment(Integer id, Payment updatedPayment) {
        return paymentRepository.findById(id)
                .map(payment -> {
                    payment.setBuyerId(updatedPayment.getBuyerId());
                    payment.setPaymentMethodId(updatedPayment.getPaymentMethodId());
                    payment.setTotalOrder(updatedPayment.getTotalOrder());
                    payment.setDate(updatedPayment.getDate());
                    payment.setConfirmation(updatedPayment.getConfirmation());
                    payment.setCodeConfirmation(updatedPayment.getCodeConfirmation());
                    payment.setCardNumber(updatedPayment.getCardNumber());
                    payment.setRefunded(updatedPayment.isRefunded());
                    return paymentRepository.save(payment);
                })
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    }

    public void deletePayment(Integer id) {
        paymentRepository.deleteById(id);
    }
}