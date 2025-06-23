package com.example.service;

import com.example.dao.PaymentMethodDao;
import com.example.dto.PaymentMethodDto;
import com.example.exception.BadRequestException;
import com.example.exception.EntityNotFoundException;
import com.example.model.PaymentMethod;
import com.example.repository.PaymentMethodRepository;
import com.example.utility.DeletionValidator;
import com.example.utility.PaymentMethodMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;
    private final DeletionValidator validator;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository, PaymentMethodMapper paymentMethodMapper, DeletionValidator validator) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodMapper = paymentMethodMapper;
        this.validator = validator;
    }

    public List<PaymentMethod> getAllPaymentMethods() {
        List<PaymentMethodDao> paymentMethodListDao = paymentMethodRepository.findAll();
        return paymentMethodListDao.stream().map(paymentMethodMapper::toModel).collect(Collectors.toList());
    }

    public PaymentMethod getPaymentMethodById(Integer id) {
        Optional<PaymentMethodDao> optionalPaymentMethod = paymentMethodRepository.findById(id);
        PaymentMethodDao paymentMethodDao = optionalPaymentMethod.orElseThrow(() -> new EntityNotFoundException("Método de pago con ID: " + id + ", no encontrado"));
        return paymentMethodMapper.toModel(paymentMethodDao);
    }

    public PaymentMethod createPaymentMethod(PaymentMethodDto paymentMethodDto) {
        return paymentMethodMapper.toModel(paymentMethodRepository.save(paymentMethodMapper.toDao(paymentMethodMapper.toModel(paymentMethodDto))));
    }

    public void deletePaymentMethod(Integer id) {
        if (!paymentMethodRepository.existsById(id))
            throw new EntityNotFoundException("Método de pago con ID: " + id + ", no encontrado");
        validator.deletionValidatorPaymentMethod(id);
        paymentMethodRepository.deleteById(id);
    }

    public PaymentMethod updatePaymentMethod(Integer id, PaymentMethodDto updatedPaymentMethodDto) {
        paymentMethodRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Método de pago con ID: " + id + ", no encontrado"));
        if (!Objects.equals(updatedPaymentMethodDto.getId(), id))
            throw new BadRequestException("El ID ingresado en el JSON no coincide con el ID de actualización: " + id);
        return paymentMethodMapper.toModel(paymentMethodRepository.save(paymentMethodMapper.toDao(paymentMethodMapper.toModel(updatedPaymentMethodDto))));
    }

    public PaymentMethod partialUpdatePaymentMethod(Integer id, Map<String, Object> updates) {
        Optional<PaymentMethodDao> optionalPaymentMethod = paymentMethodRepository.findById(id);
        PaymentMethodDao paymentMethodDaoOrigin = optionalPaymentMethod.orElseThrow(() -> new EntityNotFoundException("Método de pago con ID: " + id + ", no encontrado"));
        return paymentMethodMapper.toModel(paymentMethodRepository.save(paymentMethodMapper.parcialUpdateToDao(paymentMethodDaoOrigin, updates)));
    }
}
