package com.example.service;

import com.example.dao.RequestRefundDao;
import com.example.dto.RequestRefundDto;
import com.example.exception.BadRequestException;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.RequestRefundMapper;
import com.example.model.RequestRefund;
import com.example.repository.RequestRefundRepository;
import com.example.utility.DeletionValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestRefundService {
    private final RequestRefundRepository requestRefundRepository;
    private final RequestRefundMapper requestRefundMapper;
    private final DeletionValidator validator;

    public RequestRefundService(RequestRefundRepository requestRefundRepository, RequestRefundMapper requestRefundMapper, DeletionValidator validator) {
        this.requestRefundRepository = requestRefundRepository;
        this.requestRefundMapper = requestRefundMapper;
        this.validator = validator;
    }

    public List<RequestRefund> getAllRequestRefunds() {
        List<RequestRefundDao> refundListDao = requestRefundRepository.findAll();
        return refundListDao.stream().map(requestRefundMapper::toModel).collect(Collectors.toList());
    }

    public RequestRefund getRequestRefundById(Integer id) {
        Optional<RequestRefundDao> optionalRefund = requestRefundRepository.findById(id);
        RequestRefundDao refundDao = optionalRefund.orElseThrow(() -> new EntityNotFoundException("Solicitud de reembolso con ID: " + id + ", no encontrada"));
        return requestRefundMapper.toModel(refundDao);
    }

    public RequestRefund createRequestRefund(RequestRefundDto refundDto) {
        return requestRefundMapper.toModel(requestRefundRepository.save(requestRefundMapper.toDao(requestRefundMapper.toModel(refundDto))));
    }

    public void deleteRequestRefund(Integer id) {
        if (!requestRefundRepository.existsById(id))
            throw new EntityNotFoundException("Solicitud de reembolso con ID: " + id + ", no encontrada");
        //validator.deletionValidatorRequestRefund(id); // No se requiere por ahora
        requestRefundRepository.deleteById(id);
    }

    public RequestRefund updateRequestRefund(Integer id, RequestRefundDto updatedRefundDto) {
        requestRefundRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Solicitud de reembolso con ID: " + id + ", no encontrada"));
        if (!Objects.equals(updatedRefundDto.getId(), id))
            throw new BadRequestException("El ID ingresado en el JSON no coincide con el ID de actualización: " + id);
        return requestRefundMapper.toModel(requestRefundRepository.save(requestRefundMapper.toDao(requestRefundMapper.toModel(updatedRefundDto))));
    }

    public RequestRefund partialUpdateRequestRefund(Integer id, Map<String, Object> updates) {
        Optional<RequestRefundDao> optionalRefund = requestRefundRepository.findById(id);
        RequestRefundDao refundDaoOrigin = optionalRefund.orElseThrow(() -> new EntityNotFoundException("Solicitud de reembolso con ID: " + id + ", no encontrada"));
        return requestRefundMapper.toModel(requestRefundRepository.save(requestRefundMapper.parcialUpdateToDao(refundDaoOrigin, updates)));
    }
}
