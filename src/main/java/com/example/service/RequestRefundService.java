package com.example.service;

import com.example.dao.PaymentDao;
import com.example.dao.ProductDao;
import com.example.dao.RequestRefundDao;
import com.example.dto.RequestRefundDto;
import com.example.dto.paygate.PayGateRefundRequest;
import com.example.exception.BadRequestException;
import com.example.exception.EntityNotFoundException;
import com.example.exception.PayGateTransactionException;
import com.example.mapper.RequestRefundMapper;
import com.example.model.RequestRefund;
import com.example.repository.PaymentRepository;
import com.example.repository.ProductRepository;
import com.example.repository.RequestRefundRepository;
import com.example.utility.PayGateRequestBuilder;
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
    private final PayGateService payGateService;

    public RequestRefundService(RequestRefundRepository requestRefundRepository, RequestRefundMapper requestRefundMapper,
                                ProductRepository productRepository, OrderProcessedService orderProcessedService,
                                PaymentRepository paymentRepository, PayGateService payGateService) {
        this.requestRefundRepository = requestRefundRepository;
        this.requestRefundMapper = requestRefundMapper;
        this.productRepository = productRepository;
        this.orderProcessedService = orderProcessedService;
        this.paymentRepository = paymentRepository;
        this.payGateService = payGateService;
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
        refundDto.setConfirmation(0); // Estado inicial: pendiente

        PaymentDao paymentDao = paymentRepository.findById(refundDto.getPaymentId())
                .orElseThrow(() -> new EntityNotFoundException("Pago con ID " + refundDto.getPaymentId() + " no encontrado"));

        // Asignar buyerId automáticamente
        refundDto.setBuyerId(paymentDao.getBuyerId());

        if (paymentDao.isRefunded())
            throw new IllegalArgumentException("Este pago ya fue reembolsado, no puedes solicitar otro reembolso.");

        // tipo 1: inmediato recuperación stock (con PayGate) tipo 2: no inmediato pero con recuperación de stock tipo 3 o mas: No recuperación de stock
        if (refundDto.getRefundType() == 1) {
            // Validar existencia de ID para PayGate
            String orderId = paymentDao.getPaymentGatewayOrderId();
            String transactionId = paymentDao.getPaymentGatewayTransactionId();

            if (orderId == null || transactionId == null)
                throw new PayGateTransactionException("Este pago no tiene IDs válidos de PayGate para reembolso.");

            // Construir y enviar solicitud
            PayGateRefundRequest refundRequest = PayGateRequestBuilder.buildRefund(orderId, transactionId, refundDto.getReason(), null);
            Map<String, Object> response = payGateService.sendRefundTransaction(refundRequest);

            String responseCode = (String) response.get("code");
            if ("ERROR".equalsIgnoreCase(responseCode)) {
                throw new PayGateTransactionException("Error en PayGate: " + response.getOrDefault("error", "Mensaje no disponible"));
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> txResponse = (Map<String, Object>) response.get("transactionResponse");
            if (txResponse == null)
                throw new PayGateTransactionException("Respuesta inválida: transactionResponse es null. Respuesta completa: " + response);

            // Imprimir la respuesta completa como JSON
            try {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
                System.out.println("📥 Respuesta de PayGate (refund):\n" + json);
            } catch (JsonProcessingException e) {
                System.out.println("❌ Error al serializar la respuesta de PayGate: " + e.getMessage());
            }

            String state = (String) txResponse.get("state");
            if (!"APPROVED".equalsIgnoreCase(state) && !"PENDING".equalsIgnoreCase(state))
                throw new PayGateTransactionException("Reembolso fallido. Estado recibido: " + state);

            if ("PENDING".equalsIgnoreCase(state)) {
                String reason = (String) txResponse.getOrDefault("pendingReason", "No especificado");
                System.out.println("⏳ Reembolso pendiente por: " + reason);
            }

            // ✅ Marcar como aprobado localmente
            refundDto.setConfirmation(1);
            paymentDao.setRefunded(true);
            paymentRepository.save(paymentDao);

            // 🛒 Devolver stock
            orderProcessedService.getOrdersByPaymentId(refundDto.getPaymentId()).forEach(order -> {
                productRepository.findById(order.getProductId()).ifPresent(product -> {
                    product.setStock(product.getStock() + order.getUnits());
                    productRepository.save(product);
                });
            });
        }

        return requestRefundMapper.toModel(
                requestRefundRepository.save(
                        requestRefundMapper.toDao(
                                requestRefundMapper.toModel(refundDto)
                        )
                )
        );
    }

    public String confirmRefundById(Integer refundId, Integer state) {
        RequestRefundDao refundDao = requestRefundRepository.findById(refundId)
                .orElseThrow(() -> new EntityNotFoundException("Reembolso con ID: " + refundId + " no encontrado"));

        int currentConfirmation = refundDao.getConfirmation();

        if (currentConfirmation == 1)
            throw new BadRequestException("Ese reembolso ya había sido aprobado y no se puede cambiar");

        if (currentConfirmation == 2)
            throw new BadRequestException("Ese reembolso ya había sido rechazado y no se puede cambiar");

        if (state == 1) {
            refundDao.setConfirmation(1);

            PaymentDao paymentDao = paymentRepository.findById(refundDao.getPaymentId())
                    .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con ID: " + refundDao.getPaymentId()));

            paymentDao.setRefunded(true);
            paymentRepository.save(paymentDao);

            // tipo 1: inmediato recuperación stock (con PayGate) tipo 2: no inmediato pero con recuperación de stock tipo 3 o mas: No recuperación de stock
            if (refundDao.getRefundType() == 2) {
                orderProcessedService.getOrdersByPaymentId(refundDao.getPaymentId()).forEach(order -> {
                    ProductDao product = productRepository.findById(order.getProductId())
                            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + order.getProductId()));
                    product.setStock(product.getStock() + order.getUnits());
                    productRepository.save(product);
                });
            }

            requestRefundRepository.save(refundDao);
            return "Aprobado manualmente";
        }

        if (state == 2) {
            refundDao.setConfirmation(2);
            requestRefundRepository.save(refundDao);
            return "Rechazado manualmente";
        }

        throw new IllegalArgumentException("El estado: " + state + " no es válido (1: aprobado, 2: rechazado)");
    }

    public void deleteRequestRefund(Integer id) {
        if (!requestRefundRepository.existsById(id))
            throw new EntityNotFoundException("Solicitud de reembolso con ID: " + id + ", no encontrada");
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
