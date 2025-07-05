package com.example.service;

import com.example.dao.PaymentDao;
import com.example.dao.ProductDao;
import com.example.dao.RequestRefundDao;
import com.example.dto.RequestRefundDto;
import com.example.exception.BadRequestException;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.RequestRefundMapper;
import com.example.model.RequestRefund;
import com.example.repository.PaymentRepository;
import com.example.repository.ProductRepository;
import com.example.repository.RequestRefundRepository;
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
    private final ProductRepository productRepository;
    private final OrderProcessedService orderProcessedService;
    private final PaymentRepository paymentRepository;

    public RequestRefundService(RequestRefundRepository requestRefundRepository, RequestRefundMapper requestRefundMapper,
                                ProductRepository productRepository, OrderProcessedService orderProcessedService,
                                PaymentRepository paymentRepository) {
        this.requestRefundRepository = requestRefundRepository;
        this.requestRefundMapper = requestRefundMapper;
        this.productRepository = productRepository;
        this.orderProcessedService = orderProcessedService;
        this.paymentRepository = paymentRepository;
    }

    public String confirmRefundById(Integer refundId, Integer state) {
        Optional<RequestRefundDao> daoOptional = requestRefundRepository.findById(refundId);
        RequestRefundDao requestRefundDao = daoOptional.orElseThrow(() -> new EntityNotFoundException("Reembolso con ID: " + refundId + ", no encontrado"));

        if (requestRefundDao.getConfirmation() == 2)
            throw new BadRequestException("Ese reembolso ya habia sido rechazado y no se puede cambiar");
        if (requestRefundDao.getConfirmation() == 1)
            throw new BadRequestException("Ese reembolso ya habia sido aprobado y no se puede cambiar");

        //0 pendiente,1 aprobado, 2 rechazado
        switch (state) {
            case 0:
                throw new BadRequestException("El estado: 0 no es válido porque indica que sigue pendiente");
            case 1:
                requestRefundDao.setConfirmation(1);

                //volvemos verdadero el estado de reembolso del pago
                PaymentDao paymentDao = paymentRepository.findById(requestRefundDao.getPaymentId()).orElseThrow();
                paymentDao.setRefunded(true);
                paymentRepository.save(paymentDao);
                requestRefundRepository.save(requestRefundDao);

                //función anonima que aumenta el stock de los productos si el reembolso es aprobado con tipo 1 (que sería reembolso rapido recuperando mercancia al stock)
                if (requestRefundDao.getRefundType() == 1)
                    orderProcessedService.getOrdersByPaymentId(requestRefundDao.getPaymentId()).forEach(orderProcessed -> {
                        Optional<ProductDao> productOptional = productRepository.findById(orderProcessed.getProductId());
                        ProductDao productDao = productOptional.orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
                        productDao.setStock(productDao.getStock() + orderProcessed.getUnits());
                        productRepository.save(productDao);
                    });

                return "Aprobado";
            case 2:
                requestRefundDao.setConfirmation(2);
                requestRefundRepository.save(requestRefundDao);

                return "Rechazado";
            default:
                throw new IllegalArgumentException("El estado: " + state + " no es válido (1: aprobado, 2: rechazado)");
        }
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

        //evitamos que se confirme desde el front
        refundDto.setConfirmation(0);

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

        // Evitamos que cambien la confirmación en el front
        updatedRefundDto.setConfirmation(0);
        return requestRefundMapper.toModel(requestRefundRepository.save(requestRefundMapper.toDao(requestRefundMapper.toModel(updatedRefundDto))));
    }

    public RequestRefund partialUpdateRequestRefund(Integer id, Map<String, Object> updates) {
        Optional<RequestRefundDao> optionalRefund = requestRefundRepository.findById(id);
        RequestRefundDao refundDaoOrigin = optionalRefund.orElseThrow(() -> new EntityNotFoundException("Solicitud de reembolso con ID: " + id + ", no encontrada"));

        return requestRefundMapper.toModel(requestRefundRepository.save(requestRefundMapper.parcialUpdateToDao(refundDaoOrigin, updates)));
    }
}
