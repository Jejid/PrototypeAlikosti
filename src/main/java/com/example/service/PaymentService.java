package com.example.service;

import com.example.dao.PaymentDao;
import com.example.dao.ProductDao;
import com.example.dto.OrderProcessedDto;
import com.example.dto.PaymentDto;
import com.example.exception.BadRequestException;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.PaymentMapper;
import com.example.model.Payment;
import com.example.model.ShoppingCartOrder;
import com.example.repository.CreditCardRepository;
import com.example.repository.PaymentRepository;
import com.example.repository.ProductRepository;
import com.example.repository.ShoppingCartOrderRepository;
import com.example.utility.DeletionValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
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
    private final ProductRepository productRepository;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, DeletionValidator validator,
                          ShoppingCartOrderRepository shoppingCartRepository, CreditCardRepository creditCardRepository,
                          BuyerService buyerService, OrderProcessedService orderProcessedService1,
                          ShoppingCartOrderService shoppingCartOrderService1, ProductRepository productRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.validator = validator;
        this.shoppingCartRepository = shoppingCartRepository;
        this.creditCardRepository = creditCardRepository;
        this.buyerService = buyerService;
        this.orderProcessedService = orderProcessedService1;
        this.shoppingCartOrderService = shoppingCartOrderService1;
        this.productRepository = productRepository;
    }

    public List<Payment> getPaymentByBuyerId(Integer id) {
        List<PaymentDao> listPayment = paymentRepository.findPaymentsByBuyerId(id);
        if (listPayment.isEmpty())
            throw new EntityNotFoundException("Pagos con ID de comprador: " + id + ", no encontrados");
        return listPayment.stream().map(paymentMapper::toModel).toList();
    }

    @Transactional
    public Payment createPayment(PaymentDto paymentDto) {
        // 1. Verificar que el comprador y  orden existan
        buyerService.getBuyerById(paymentDto.getBuyerId());
        List<ShoppingCartOrder> shoppingCartOrderList = shoppingCartOrderService.getOrderByBuyerId(paymentDto.getBuyerId());


        // 2. Validar tarjeta si aplica
        if (paymentDto.getPaymentMethodId() == 2 && paymentDto.getCardNumber() != null) {
            creditCardRepository.findByCardNumberAndBuyerId(paymentDto.getCardNumber(), paymentDto.getBuyerId())
                    .orElseThrow(() -> new BadRequestException("Esa tarjeta no pertenece al comprador indicado, ABRE VENTANA DE CREACIÓN DE CREDIT_CARDS"));
        }

        // 3. Reducir el stock (para reservar ese pedido) o lanzar alerta de producto agotado
        shoppingCartOrderList.forEach(shoppinCartOrder -> {
            Optional<ProductDao> productOptional = productRepository.findById(shoppinCartOrder.getProductId());
            ProductDao productDao = productOptional.orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
            //System.out.println("queda en el stock: " + (productDao.getStock() - shoppinCartOrder.getUnits()));
            if (productDao.getStock() - shoppinCartOrder.getUnits() < 0) {
                throw new EntityNotFoundException("No se puede crear el pago porque se agotó el stock del producto: " + productDao.getId() + " por favor cambia o elimina el producto del carrito");
            } else productDao.setStock(productDao.getStock() - shoppinCartOrder.getUnits());
            productRepository.save(productDao);
        });

        // 4. Calcular el total del pedido
        Integer totalOrder = shoppingCartRepository.sumTotalProductsByBuyerId(paymentDto.getBuyerId());
        paymentDto.setTotalOrder(totalOrder != null ? totalOrder : 0);

        // 5. Inicializar campos del pago
        paymentDto.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        paymentDto.setConfirmation(0);
        paymentDto.setCodeConfirmation(null);
        paymentDto.setRefunded(false);


        // 6. Guardar el pago
        Payment savedPayment = paymentMapper.toModel(
                paymentRepository.save(
                        paymentMapper.toDao(
                                paymentMapper.toModel(paymentDto)
                        )
                )
        );

        // 7. Convertir ítems del carrito en órdenes procesadas
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

        // 8. Guardar órdenes procesadas (ventas)
        orderProcessedService.createMultipleOrdersProcessed(orderList);

        // 9. Limpiar el carrito
        shoppingCartOrderService.deleteOrderByBuyerId(paymentDto.getBuyerId());


        return savedPayment;
    }

    public String confirmPaymenteById(Integer paymentId, Integer state) {
        Optional<PaymentDao> daoOptional = paymentRepository.findById(paymentId);
        PaymentDao dao = daoOptional.orElseThrow(() -> new EntityNotFoundException("Pago con ID: " + paymentId + ", no encontrado"));

        if (dao.getConfirmation() == 2)
            throw new BadRequestException("Ese pago ya habia sido rechazado y no se puede cambiar");
        if (dao.getConfirmation() == 1)
            throw new BadRequestException("Ese pago ya habia sido aprobado y no se puede cambiar");

        switch (state) {
            case 0:
                throw new BadRequestException("El estado: 0 no es válido porque indica que sigue pendiente");
            case 1:
                dao.setCodeConfirmation(ThreadLocalRandom.current().nextInt(1100, 10000));
                dao.setConfirmation(1);
                paymentRepository.save(dao);

                return "Aprobado";
            case 2:
                dao.setConfirmation(2);
                paymentRepository.save(dao);

                //funcion anonima que aumenta los stock de los productos del pago rechazado
                orderProcessedService.getOrdersByPaymentId(paymentId).forEach(orderProcessed -> {
                    Optional<ProductDao> productOptional = productRepository.findById(orderProcessed.getProductId());
                    ProductDao productDao = productOptional.orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
                    productDao.setStock(productDao.getStock() + orderProcessed.getUnits());
                    productRepository.save(productDao);
                });

                return "Rechazado";
            default:
                throw new IllegalArgumentException("El estado: " + state + " no es válido (1: aprobado, 2: rechazado)");

        }

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

        //evitamos que cambien en el front la confirmación de pago
        updatedPaymentDto.setConfirmation(0);
        return paymentMapper.toModel(paymentRepository.save(paymentMapper.toDao(paymentMapper.toModel(updatedPaymentDto))));
    }

    public Payment partialUpdatePayment(Integer id, Map<String, Object> updates) {
        Optional<PaymentDao> optionalPayment = paymentRepository.findById(id);
        PaymentDao paymentDaoOrigin = optionalPayment.orElseThrow(() -> new EntityNotFoundException("Pago con ID: " + id + ", no encontrado"));
        return paymentMapper.toModel(paymentRepository.save(paymentMapper.parcialUpdateToDao(paymentDaoOrigin, updates)));
    }
}
