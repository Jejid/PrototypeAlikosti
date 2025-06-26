package com.example.service;

import com.example.dao.PaymentDao;
import com.example.dto.OrderProcessedDto;
import com.example.dto.PaymentDto;
import com.example.exception.BadRequestException;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.PaymentMapper;
import com.example.model.Payment;
import com.example.model.ShoppingCartOrder;
import com.example.repository.CreditCardRepository;
import com.example.repository.PaymentRepository;
import com.example.repository.ShoppingCartOrderRepository;
import com.example.utility.DeletionValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final ShoppingCartOrderRepository shoppingCartRepository;
    private final CreditCardRepository creditCardRepository;
    private final BuyerService buyerService;
    private final OrderProcessedService orderProcessedService;
    private final ShoppingCartOrderService shoppingCartOrderService;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, DeletionValidator validator, ShoppingCartOrderRepository shoppingCartRepository, CreditCardRepository creditCardRepository, BuyerService buyerService, OrderProcessedService orderProcessedService1, ShoppingCartOrderService shoppingCartOrderService1) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.validator = validator;
        this.shoppingCartRepository = shoppingCartRepository;
        this.creditCardRepository = creditCardRepository;
        this.buyerService = buyerService;
        this.orderProcessedService = orderProcessedService1;
        this.shoppingCartOrderService = shoppingCartOrderService1;
    }

    public List<Payment> getPaymentByBuyerId(Integer id) {
        List<PaymentDao> listPayment = paymentRepository.findPaymentsByBuyerId(id);
        if (listPayment.isEmpty())
            throw new EntityNotFoundException("Pagos con ID de comprador: " + id + ", no encontrados");
        return listPayment.stream().map(paymentMapper::toModel).toList();
    }


    public Payment createPayment(PaymentDto paymentDto) {
        // 1. Verificar que el comprador y  orden existan
        buyerService.getBuyerById(paymentDto.getBuyerId());
        List<ShoppingCartOrder> shoppingCartOrderList = shoppingCartOrderService.getOrderByBuyerId(paymentDto.getBuyerId());
        //if (shoppingCartOrderList.isEmpty()) return null;

        // 2. Calcular el total del pedido
        Integer totalOrder = shoppingCartRepository.sumTotalProductsByBuyerId(paymentDto.getBuyerId());
        paymentDto.setTotalOrder(totalOrder != null ? totalOrder : 0);

        // 3. Inicializar campos del pago
        paymentDto.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        paymentDto.setConfirmation(0);
        paymentDto.setCodeConfirmation(null);
        paymentDto.setRefunded(false);

        // 4. Validar tarjeta si aplica
        if (paymentDto.getPaymentMethodId() == 2) {
            creditCardRepository.findByCardNumberAndBuyerId(paymentDto.getCardNumber(), paymentDto.getBuyerId())
                    .orElseThrow(() -> new BadRequestException("Esa tarjeta no pertenece al comprador indicado, ABRE VENTANA DE CREACIÓN DE CREDIT_CARDS"));
        }

        // 5. Guardar el pago
        Payment savedPayment = paymentMapper.toModel(
                paymentRepository.save(
                        paymentMapper.toDao(
                                paymentMapper.toModel(paymentDto)
                        )
                )
        );

        // 6. Convertir ítems del carrito en órdenes procesadas
        List<OrderProcessedDto> orderList = shoppingCartOrderList
                .stream()
                .map(order -> {
                    OrderProcessedDto dto = new OrderProcessedDto();
                    dto.setPaymentId(savedPayment.getId());
                    dto.setProductId(order.getProductId());
                    dto.setUnits(order.getUnits());
                    dto.setTotalProduct(order.getTotalProduct());
                    return dto;
                })
                .toList();

        // 7. Guardar órdenes procesadas (ventas)
        orderProcessedService.createMultipleOrdersProcessed(orderList);

        // 8. Limpiar el carrito
        shoppingCartOrderService.deleteOrderByBuyerId(paymentDto.getBuyerId());

        return savedPayment;
    }


    // ------- Métodos Basicos-------//
    public List<Payment> getAllPayments() {
        List<PaymentDao> paymentListDao = paymentRepository.findAll();
        return paymentListDao.stream().map(paymentMapper::toModel).collect(Collectors.toList());
    }

    public Payment getPaymentById(Integer id) {
        Optional<PaymentDao> optionalPayment = paymentRepository.findById(id);
        PaymentDao paymentDao = optionalPayment.orElseThrow(() -> new EntityNotFoundException("Pago con ID: " + id + ", no encontrado"));
        return paymentMapper.toModel(paymentDao);
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
