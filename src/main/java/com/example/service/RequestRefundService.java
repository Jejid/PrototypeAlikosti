package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.repository.RequestRefundRepository;
import com.example.dao.RequestRefundDao;
import com.example.model.RequestRefund;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RequestRefundService {
    private final RequestRefundRepository requestRefundRepository;

    public RequestRefundService(RequestRefundRepository requestRefundRepository) {
        this.requestRefundRepository = requestRefundRepository;
    }

    public List<RequestRefund> getAllRefunds() {
        List<RequestRefundDao> daoList = requestRefundRepository.findAll();
        List<RequestRefund> refundList = new ArrayList<>();

        for (RequestRefundDao dao : daoList) {
            RequestRefund refund = new RequestRefund();
            refund.setId(dao.getId());
            refund.setBuyerId(dao.getBuyerId());
            refund.setPaymentId(dao.getPaymentId());
            refund.setConfirmation(dao.getConfirmation());
            refund.setRefundType(dao.getRefundType());
            refundList.add(refund);
        }
        return refundList;
    }

    public RequestRefund getRefundById(Integer id) {
        Optional<RequestRefundDao> dao = requestRefundRepository.findById(id);

        RequestRefundDao found = dao.orElseThrow(() ->
                new EntityNotFoundException("Solicitud de reembolso con ID: " + id + " no encontrada"));

        RequestRefund refund = new RequestRefund();
        refund.setId(found.getId());
        refund.setBuyerId(found.getBuyerId());
        refund.setPaymentId(found.getPaymentId());
        refund.setConfirmation(found.getConfirmation());
        refund.setRefundType(found.getRefundType());

        return refund;
    }

    public RequestRefund createRefund(RequestRefund refund) {
        RequestRefundDao toSave = new RequestRefundDao();
        toSave.setBuyerId(refund.getBuyerId());
        toSave.setPaymentId(refund.getPaymentId());
        toSave.setConfirmation(refund.getConfirmation());
        toSave.setRefundType(refund.getRefundType());

        RequestRefundDao saved = requestRefundRepository.save(toSave);

        return new RequestRefund(
                saved.getId(),
                saved.getBuyerId(),
                saved.getPaymentId(),
                saved.getConfirmation(),
                saved.getRefundType());
    }

    public RequestRefund updateRefund(Integer id, RequestRefund updatedRefund) {
        Optional<RequestRefundDao> optionalDao = requestRefundRepository.findById(id);

        if (optionalDao.isEmpty()) {
            throw new EntityNotFoundException("Solicitud de reembolso con ID: " + id + " no encontrada");
        }

        RequestRefundDao requestRefundDao = optionalDao.get();
        requestRefundDao.setBuyerId(updatedRefund.getBuyerId());
        requestRefundDao.setPaymentId(updatedRefund.getPaymentId());
        requestRefundDao.setConfirmation(updatedRefund.getConfirmation());
        requestRefundDao.setRefundType(updatedRefund.getRefundType());

        requestRefundRepository.save(requestRefundDao);

        return new RequestRefund(
                requestRefundDao.getId(),
                requestRefundDao.getBuyerId(),
                requestRefundDao.getPaymentId(),
                requestRefundDao.getConfirmation(),
                requestRefundDao.getRefundType());
    }

    public void deleteRefund(Integer id) {
        if (requestRefundRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Solicitud de reembolso con ID " + id + " no encontrada");
        }
        requestRefundRepository.deleteById(id);
    }

    public RequestRefund partialUpdateRefund(Integer id, Map<String, Object> updates) {
        Optional<RequestRefundDao> optionalDao = requestRefundRepository.findById(id);

        if (optionalDao.isEmpty()) {
            throw new EntityNotFoundException("Solicitud de reembolso con ID: " + id + " no encontrada");
        }

        final RequestRefundDao dao = optionalDao.get();

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "buyerId":
                        if (value instanceof Number) dao.setBuyerId((Integer) value);
                        break;
                    case "paymentId":
                        if (value instanceof Number) dao.setPaymentId((Integer) value);
                        break;
                    case "confirmation":
                        if (value instanceof Number) dao.setConfirmation((Integer) value);
                        break;
                    case "refundType":
                        if (value instanceof Number) dao.setRefundType((Integer) value);
                        break;
                    default:
                        throw new IllegalArgumentException("El campo " + key + " no es válido o no existe para actualización.");
                }
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Tipo de dato incorrecto para el campo " + key);
            }
        });

        requestRefundRepository.save(dao);

        return new RequestRefund(
                dao.getId(),
                dao.getBuyerId(),
                dao.getPaymentId(),
                dao.getConfirmation(),
                dao.getRefundType());
    }
}
