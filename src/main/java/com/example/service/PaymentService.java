package com.example.service;

import com.example.dao.PaymentDao;
import com.example.exception.EntityNotFoundException;
import com.example.model.Payment;
import com.example.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getAllPayments() {
        List<PaymentDao> paymentListDao = paymentRepository.findAll();
        List<Payment> paymentList = new ArrayList<>();

        for (PaymentDao paymentDao : paymentListDao) {
            Payment payment = new Payment();
            payment.setId(paymentDao.getId());
            payment.setBuyerId(paymentDao.getBuyerId());
            payment.setPaymentMethodId(paymentDao.getPaymentMethodId());
            payment.setTotalOrder(paymentDao.getTotalOrder());
            payment.setDate(paymentDao.getDate());
            payment.setConfirmation(paymentDao.getConfirmation());
            payment.setCodeConfirmation(paymentDao.getCodeConfirmation());
            payment.setCardNumber(paymentDao.getCardNumber());
            payment.setRefunded(paymentDao.isRefunded());
            paymentList.add(payment);
        }
        return paymentList;
    }

    public Payment getPaymentById(Integer id) {
        Optional<PaymentDao> paymentDao = paymentRepository.findById(id);

        PaymentDao paymentDao1 = paymentDao.orElseThrow(() -> new EntityNotFoundException("Pago con ID: " + id + ", no encontrado"));

        Payment payment = new Payment();
        payment.setId(paymentDao1.getId());
        payment.setBuyerId(paymentDao1.getBuyerId());
        payment.setPaymentMethodId(paymentDao1.getPaymentMethodId());
        payment.setTotalOrder(paymentDao1.getTotalOrder());
        payment.setDate(paymentDao1.getDate());
        payment.setConfirmation(paymentDao1.getConfirmation());
        payment.setCodeConfirmation(paymentDao1.getCodeConfirmation());
        payment.setCardNumber(paymentDao1.getCardNumber());
        payment.setRefunded(paymentDao1.isRefunded());

        return payment;
    }

    public Payment createPayment(Payment payment) {
        PaymentDao paymentToUpload = new PaymentDao();

        paymentToUpload.setBuyerId(payment.getBuyerId());
        paymentToUpload.setPaymentMethodId(payment.getPaymentMethodId());
        paymentToUpload.setTotalOrder(payment.getTotalOrder());
        paymentToUpload.setDate(payment.getDate());
        paymentToUpload.setConfirmation(payment.getConfirmation());
        paymentToUpload.setCodeConfirmation(payment.getCodeConfirmation());
        paymentToUpload.setCardNumber(payment.getCardNumber());
        paymentToUpload.setRefunded(payment.isRefunded());

        PaymentDao createdPayment = paymentRepository.save(paymentToUpload);

        return new Payment(
                createdPayment.getId(),
                createdPayment.getBuyerId(),
                createdPayment.getPaymentMethodId(),
                createdPayment.getTotalOrder(),
                createdPayment.getDate(),
                createdPayment.getConfirmation(),
                createdPayment.getCodeConfirmation(),
                createdPayment.getCardNumber(),
                createdPayment.isRefunded()
        );
    }

    public void deletePayment(Integer id) {
        if (paymentRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Pago con ID " + id + ", no encontrado");
        }
        paymentRepository.deleteById(id);
    }

    public Payment updatePayment(Integer id, Payment updatedPayment) {
        Optional<PaymentDao> optionalPaymentDao = paymentRepository.findById(id);

        if (optionalPaymentDao.isEmpty()) {
            throw new EntityNotFoundException("Pago con ID: " + id + " no encontrado");
        }

        PaymentDao paymentDao = optionalPaymentDao.get();

        paymentDao.setBuyerId(updatedPayment.getBuyerId());
        paymentDao.setPaymentMethodId(updatedPayment.getPaymentMethodId());
        paymentDao.setTotalOrder(updatedPayment.getTotalOrder());
        paymentDao.setDate(updatedPayment.getDate());
        paymentDao.setConfirmation(updatedPayment.getConfirmation());
        paymentDao.setCodeConfirmation(updatedPayment.getCodeConfirmation());
        paymentDao.setCardNumber(updatedPayment.getCardNumber());
        paymentDao.setRefunded(updatedPayment.isRefunded());

        paymentRepository.save(paymentDao);

        return new Payment(
                paymentDao.getId(),
                paymentDao.getBuyerId(),
                paymentDao.getPaymentMethodId(),
                paymentDao.getTotalOrder(),
                paymentDao.getDate(),
                paymentDao.getConfirmation(),
                paymentDao.getCodeConfirmation(),
                paymentDao.getCardNumber(),
                paymentDao.isRefunded()
        );
    }

    public Payment partialUpdatePayment(Integer id, Map<String, Object> updates) {
        Optional<PaymentDao> optionalPaymentDao = paymentRepository.findById(id);
        if (optionalPaymentDao.isEmpty()) {
            throw new EntityNotFoundException("Pago con ID: " + id + " no encontrado");
        }

        final PaymentDao paymentDao = optionalPaymentDao.get();

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "buyerId":
                        if (value instanceof Number) paymentDao.setBuyerId(((Number) value).intValue());
                        break;
                    case "paymentMethodId":
                        if (value instanceof Number) paymentDao.setPaymentMethodId(((Number) value).intValue());
                        break;
                    case "totalOrder":
                        if (value instanceof Number) paymentDao.setTotalOrder(((Number) value).intValue());
                        break;
                    case "date":
                        if (value == null || value instanceof String) paymentDao.setDate((String) value);
                        break;
                    case "confirmation":
                        if (value instanceof Number) paymentDao.setConfirmation(((Number) value).intValue());
                        break;
                    case "codeConfirmation":
                        if (value == null || value instanceof Number)
                            paymentDao.setCodeConfirmation(value == null ? null : ((Number) value).intValue());
                        break;
                    case "cardNumber":
                        if (value == null || value instanceof String) paymentDao.setCardNumber((String) value);
                        break;
                    case "refunded":
                        if (value instanceof Boolean) paymentDao.setRefunded((Boolean) value);
                        break;
                    default:
                        throw new IllegalArgumentException("El campo " + key + " no es válido o no existe para actualización.");
                }
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Tipo de dato incorrecto para el campo " + key);
            }
        });

        paymentRepository.save(paymentDao);

        return new Payment(
                paymentDao.getId(),
                paymentDao.getBuyerId(),
                paymentDao.getPaymentMethodId(),
                paymentDao.getTotalOrder(),
                paymentDao.getDate(),
                paymentDao.getConfirmation(),
                paymentDao.getCodeConfirmation(),
                paymentDao.getCardNumber(),
                paymentDao.isRefunded()
        );
    }

}
