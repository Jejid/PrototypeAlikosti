package com.example.service;

import com.example.dao.PaymentDao;
import com.example.dto.PaymentDto;
import com.example.exception.BadRequestException;
import com.example.exception.EntityNotFoundException;
import com.example.model.Payment;
import com.example.repository.PaymentRepository;
import com.example.utility.DeletionValidator;
import com.example.utility.PaymentMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final DeletionValidator validator;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, DeletionValidator validator) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.validator = validator;
    }

    public List<Payment> getAllPayments() {
        List<PaymentDao> paymentListDao = paymentRepository.findAll();
        return paymentListDao.stream().map(paymentMapper::toModel).collect(Collectors.toList());
    }

    public Payment getPaymentById(Integer id) {
        Optional<PaymentDao> optionalPayment = paymentRepository.findById(id);
        PaymentDao paymentDao = optionalPayment.orElseThrow(() -> new EntityNotFoundException("Pago con ID: " + id + ", no encontrado"));
        return paymentMapper.toModel(paymentDao);
    }

    public Payment createPayment(PaymentDto paymentDto) {
        return paymentMapper.toModel(paymentRepository.save(paymentMapper.toDao(paymentMapper.toModel(paymentDto))));
    }

    public void deletePayment(Integer id) {
        if (!paymentRepository.existsById(id))
            throw new EntityNotFoundException("Pago con ID: " + id + ", no encontrado");
        validator.deletionValidatorPayment(id);
        paymentRepository.deleteById(id);
    }

    public Payment updatePayment(Integer id, PaymentDto updatedPaymentDto) {
        paymentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pago con ID: " + id + ", no encontrado"));
        if (!Objects.equals(updatedPaymentDto.getId(), id))
            throw new BadRequestException("El ID ingresado en el JSON no coincide con el ID de actualización: " + id);
        return paymentMapper.toModel(paymentRepository.save(paymentMapper.toDao(paymentMapper.toModel(updatedPaymentDto))));
    }

    public Payment partialUpdatePayment(Integer id, Map<String, Object> updates) {
        Optional<PaymentDao> optionalPayment = paymentRepository.findById(id);
        PaymentDao paymentDaoOrigin = optionalPayment.orElseThrow(() -> new EntityNotFoundException("Pago con ID: " + id + ", no encontrado"));
        return paymentMapper.toModel(paymentRepository.save(paymentMapper.parcialUpdateToDao(paymentDaoOrigin, updates)));
    }
}
