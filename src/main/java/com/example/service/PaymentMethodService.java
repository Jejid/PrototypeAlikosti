package com.example.service;

import com.example.dao.PaymentMethodDao;
import com.example.exception.EntityNotFoundException;
import com.example.model.PaymentMethod;
import com.example.repository.PaymentMethodRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public List<PaymentMethod> getAllPaymentMethods() {
        List<PaymentMethodDao> daoList = paymentMethodRepository.findAll();
        List<PaymentMethod> methodList = new ArrayList<>();

        for (PaymentMethodDao dao : daoList) {
            methodList.add(new PaymentMethod(
                    dao.getId(),
                    dao.getName(),
                    dao.getDescription()
            ));
        }
        return methodList;
    }

    public PaymentMethod getPaymentMethodById(Integer id) {
        PaymentMethodDao dao = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Método de pago con ID: " + id + " no encontrado"));

        return new PaymentMethod(
                dao.getId(),
                dao.getName(),
                dao.getDescription()
        );
    }

    public PaymentMethod createPaymentMethod(PaymentMethod method) {
        PaymentMethodDao dao = new PaymentMethodDao();
        dao.setName(method.getName());
        dao.setDescription(method.getDescription());

        PaymentMethodDao saved = paymentMethodRepository.save(dao);

        return new PaymentMethod(
                saved.getId(),
                saved.getName(),
                saved.getDescription()
        );
    }

    public PaymentMethod updatePaymentMethod(Integer id, PaymentMethod updated) {
        PaymentMethodDao dao = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Método de pago con ID: " + id + " no encontrado"));

        dao.setName(updated.getName());
        dao.setDescription(updated.getDescription());

        PaymentMethodDao saved = paymentMethodRepository.save(dao);

        return new PaymentMethod(
                saved.getId(),
                saved.getName(),
                saved.getDescription()
        );
    }

    public void deletePaymentMethod(Integer id) {
        if (paymentMethodRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Método de pago con ID " + id + " no encontrado");
        }
        paymentMethodRepository.deleteById(id);
    }

    public PaymentMethod partialUpdatePaymentMethod(Integer id, Map<String, Object> updates) {
        PaymentMethodDao dao = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Método de pago con ID: " + id + " no encontrado"));

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "name" -> {
                        if (value == null || value instanceof String) dao.setName((String) value);
                    }
                    case "description" -> {
                        if (value == null || value instanceof String) dao.setDescription((String) value);
                    }
                    default -> throw new IllegalArgumentException("Campo " + key + " no válido para actualización.");
                }
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Tipo de dato incorrecto para el campo " + key);
            }
        });

        PaymentMethodDao saved = paymentMethodRepository.save(dao);

        return new PaymentMethod(
                saved.getId(),
                saved.getName(),
                saved.getDescription()
        );
    }
}
