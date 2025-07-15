package com.example.service;

import com.example.dao.PaymentDao;
import com.example.dao.ProductDao;
import com.example.dao.RequestRefundDao;
import com.example.dto.RequestRefundDto;
import com.example.dto.payu.PayuRefundRequest;
import com.example.exception.BadRequestException;
import com.example.exception.EntityNotFoundException;
import com.example.exception.PayuTransactionException;
import com.example.mapper.RequestRefundMapper;
import com.example.model.RequestRefund;
import com.example.repository.PaymentRepository;
import com.example.repository.ProductRepository;
import com.example.repository.RequestRefundRepository;
import com.example.utility.PayuRequestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestRefundService {
    private final RequestRefundRepository requestRefundRepository;
    private final RequestRefundMapper requestRefundMapper;
    private final ProductRepository productRepository;
    private final OrderProcessedService orderProcessedService;
    private final PaymentRepository paymentRepository;
    private final PayuService payuService;

    public RequestRefundService(RequestRefundRepository requestRefundRepository, RequestRefundMapper requestRefundMapper,
                                ProductRepository productRepository, OrderProcessedService orderProcessedService,
                                PaymentRepository paymentRepository, PayuService payuService) {
        this.requestRefundRepository = requestRefundRepository;
        this.requestRefundMapper = requestRefundMapper;
        this.productRepository = productRepository;
        this.orderProcessedService = orderProcessedService;
        this.paymentRepository = paymentRepository;
        this.payuService = payuService;
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

                //función anonima que aumenta el stock de los productos si el reembolso es aprobado con tipo 2, es decir que no es pago Pasarela
                //pero aun se recupera el stock
                if (requestRefundDao.getRefundType() == 2)
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

    @Transactional
    public RequestRefund createRequestRefund(RequestRefundDto refundDto) {

        refundDto.setConfirmation(0); // estado inicial: pendiente

        PaymentDao paymentDao = paymentRepository.findById(refundDto.getPaymentId())
                .orElseThrow(() -> new EntityNotFoundException("Pago con ID " + refundDto.getPaymentId() + " no encontrado"));

        if (paymentDao.isRefunded())
            throw new IllegalArgumentException("Este pago ya fue reembolsado, no puedes solicitar otro reembolso");

        // Si es reembolso automático por PayU
        if (refundDto.getRefundType() == 1) {


            if (paymentDao.getPaymentGatewayOrderId() == null || paymentDao.getPaymentGatewayTransactionId() == null) {
                throw new PayuTransactionException("Este pago no tiene IDs válidos de PayU para solicitar reembolso.");
            }

            // Construir solicitud
            PayuRefundRequest refundRequest = PayuRequestBuilder.buildRefund(
                    paymentDao.getPaymentGatewayOrderId(),
                    paymentDao.getPaymentGatewayTransactionId(),
                    "Razón genérica de reembolso",
                    null // no parcial
            );

            Map<String, Object> response = payuService.sendRefundTransaction(refundRequest);

            // 🔍 Verificación de código general
            String responseCode = (String) response.get("code");
            if ("ERROR".equalsIgnoreCase(responseCode)) {
                String errorMessage = (String) response.getOrDefault("error", "Error desconocido desde PayU al solicitar el reembolso.");
                throw new PayuTransactionException("Error en PayU: " + errorMessage);
            }

            // 🔍 Validación de transactionResponse
            @SuppressWarnings("unchecked")
            Map<String, Object> txResponse = (Map<String, Object>) response.get("transactionResponse");
            if (txResponse == null) {
                throw new PayuTransactionException("❌ Respuesta inválida: transactionResponse es null. Respuesta completa: " + response);
            }

            // Mostrar respuesta completa para debug
            try {
                ObjectMapper mapper = new ObjectMapper();
                System.out.println("📥 Respuesta de PayU (refund):\n" +
                        mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
            } catch (JsonProcessingException e) {
                System.out.println("❌ Error al serializar la respuesta de PayU: " + e.getMessage());
            }

            // Procesar estado
            String state = (String) txResponse.get("state");
            if (!"APPROVED".equalsIgnoreCase(state) && !"PENDING".equalsIgnoreCase(state)) {
                throw new PayuTransactionException("Reembolso fallido. Estado recibido: " + state);
            }

            if ("PENDING".equalsIgnoreCase(state)) {
                String pendingReason = (String) txResponse.getOrDefault("pendingReason", "No especificado");
                System.out.println("⏳ Reembolso pendiente por: " + pendingReason);
            }

            // 🟢 Marcar como aprobado en la app (aunque PayU diga PENDING, lo aceptamos como válido)
            refundDto.setConfirmation(1);
            paymentDao.setRefunded(true);
            paymentRepository.save(paymentDao);

            // 🛒 Devolver stock si es tipo 1
            orderProcessedService.getOrdersByPaymentId(refundDto.getPaymentId()).forEach(order -> {
                ProductDao product = productRepository.findById(order.getProductId())
                        .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID " + order.getProductId()));
                product.setStock(product.getStock() + order.getUnits());
                productRepository.save(product);
            });
        }

        // Guardar solicitud en base de datos
        return requestRefundMapper.toModel(
                requestRefundRepository.save(
                        requestRefundMapper.toDao(requestRefundMapper.toModel(refundDto))
                )
        );
    }


    public void deleteRequestRefund(Integer id) {
        if (!requestRefundRepository.existsById(id))
            throw new EntityNotFoundException("Solicitud de reembolso con ID: " + id + ", no encontrada");
        //validator.deletionValidatorRequestRefund(id); // No se requiere por ahora
        requestRefundRepository.deleteById(id);
    }

    /*public RequestRefund updateRequestRefund(Integer id, RequestRefundDto updatedRefundDto) {
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
    }*/
}
