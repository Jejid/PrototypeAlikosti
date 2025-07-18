package com.example.service;

import com.example.dao.PaymentDao;
import com.example.dao.ProductDao;
import com.example.dto.BuyerDto;
import com.example.dto.CreditCardDto;
import com.example.dto.OrderProcessedDto;
import com.example.dto.PaymentDto;
import com.example.dto.payu.PayuPaymentRequest;
import com.example.exception.BadRequestException;
import com.example.exception.EntityNotFoundException;
import com.example.exception.PayuTransactionException;
import com.example.mapper.BuyerMapper;
import com.example.mapper.CreditCardMapper;
import com.example.mapper.OrderProcessedMapper;
import com.example.mapper.PaymentMapper;
import com.example.model.Buyer;
import com.example.model.Payment;
import com.example.model.ShoppingCartOrder;
import com.example.repository.CreditCardRepository;
import com.example.repository.PaymentRepository;
import com.example.repository.ProductRepository;
import com.example.repository.ShoppingCartOrderRepository;
import com.example.utility.DeletionValidator;
import com.example.utility.PayuRequestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final BuyerMapper buyerMapper;
    private final DeletionValidator validator;
    private final CreditCardRepository creditCardRepository;
    private final BuyerService buyerService;
    private final OrderProcessedService orderProcessedService;
    private final ShoppingCartOrderService shoppingCartOrderService;
    private final ProductRepository productRepository;
    private final ShoppingCartOrderRepository shoppingCartOrderRepository;
    private final PayuService payuService;
    private final OrderProcessedMapper orderProcessedMapper;
    private final CreditCardMapper creditCardMapper;
    private final CreditCardService creditCardService;

    public static final int PAYMENT_METHOD_CARD = 2;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, BuyerMapper buyerMapper, DeletionValidator validator,
                          CreditCardRepository creditCardRepository,
                          BuyerService buyerService, OrderProcessedService orderProcessedService1,
                          ShoppingCartOrderService shoppingCartOrderService1, ProductRepository productRepository,
                          ShoppingCartOrderRepository shoppingCartOrderRepository, PayuService payuService, OrderProcessedMapper orderProcessedMapper,
                          CreditCardMapper creditCardMapper, CreditCardService creditCardService) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.buyerMapper = buyerMapper;
        this.validator = validator;
        this.creditCardRepository = creditCardRepository;
        this.buyerService = buyerService;
        this.orderProcessedService = orderProcessedService1;
        this.shoppingCartOrderService = shoppingCartOrderService1;
        this.productRepository = productRepository;
        this.shoppingCartOrderRepository = shoppingCartOrderRepository;
        this.payuService = payuService;
        this.orderProcessedMapper = orderProcessedMapper;
        this.creditCardMapper = creditCardMapper;
        this.creditCardService = creditCardService;
    }

    public List<Payment> getPaymentByBuyerId(Integer id) {
        List<PaymentDao> listPayment = paymentRepository.findPaymentsByBuyerId(id);
        if (listPayment.isEmpty())
            throw new EntityNotFoundException("Pagos con ID de comprador: " + id + ", no encontrados");
        return listPayment.stream().map(paymentMapper::toModel).toList();
    }

    @Transactional
    public Payment createPayment(PaymentDto paymentDto) {
        // 1. Verificar que el comprador y orden existan
        Buyer buyer = buyerService.getBuyerById(paymentDto.getBuyerId());
        BuyerDto buyerDto = buyerMapper.toDto(buyer);
        List<ShoppingCartOrder> shoppingCartOrderList = shoppingCartOrderService.getOrderByBuyerId(paymentDto.getBuyerId());

        // 2. Calcular el total real desde BD
        Integer totalOrder = shoppingCartOrderRepository.sumTotalProductsByBuyerId(paymentDto.getBuyerId());
        paymentDto.setTotalOrder(totalOrder != null ? totalOrder : 0);

        // 3. Inicializar campos comunes
        paymentDto.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a")));
        paymentDto.setRefunded(false);
        paymentDto.setConfirmation(0);
        paymentDto.setCodeConfirmation(null);

        // 4. Convertir ítems del carrito en órdenes procesadas
        List<OrderProcessedDto> orderList = shoppingCartOrderList.stream()
                .map(order -> {
                    OrderProcessedDto dto = new OrderProcessedDto();
                    dto.setProductId(order.getProductId());
                    dto.setUnits(order.getUnits());
                    dto.setTotalProduct(order.getTotalProduct());
                    return dto;
                }).toList();

        // 5. Descontar stock
        updateStock(orderList, "decrease");

        // 6. Procesar pago con tarjeta (PayU)
        if (paymentDto.getPaymentMethodId() == PAYMENT_METHOD_CARD) {

            PayuPaymentRequest payuRequest = new PayuPaymentRequest();

            //6.1 agregar tarjeta nueva sin token
            if ((paymentDto.getCardNumber() != null && paymentDto.getCvcCode() != null && paymentDto.getCardDate() != null && paymentDto.getTokenizedCode() == null)) {

                //asignamos los datos de tarjeta traidos desde la interfaz de pago
                CreditCardDto creditCardDto = new CreditCardDto();
                creditCardDto.setBuyerId(paymentDto.getBuyerId());
                creditCardDto.setCardNumber(paymentDto.getCardNumber());
                creditCardDto.setName(paymentDto.getCardName());
                creditCardDto.setCardDate(paymentDto.getCardDate());
                creditCardDto.setCvcCode(paymentDto.getCvcCode());
                creditCardDto.setFranchise(paymentDto.getFranchise());
                creditCardDto.setCardType(paymentDto.getCardType());
                creditCardDto.setBank(paymentDto.getBank());

                //guardar tajerta y su token
                creditCardService.createCreditCard(creditCardDto);

                payuRequest = PayuRequestBuilder.buildPayment(paymentDto, buyerDto, creditCardDto);
            }
            //6.2 pago con token de tarjeta
            else if ((paymentDto.getCardNumber() == null && paymentDto.getTokenizedCode() != null && paymentDto.getCvcCode() != null)) {
                payuRequest = PayuRequestBuilder.buildPaymentWithToken(paymentDto, buyerDto, paymentDto.getTokenizedCode(), paymentDto.getCvcCode());
                paymentDto.setCardNumber(paymentDto.getTokenizedCode());
            } else
                throw new BadRequestException("Para pago con token de tarjet debes ingresar: el token y el código de seguridad cvc." +
                        " Para pago con tarjeta directa debe ingresar: el número de tarjeta, la fecha de expiración y el código de seguridad cvc.");


            Map<String, Object> payuResponse = payuService.sendPaymentTransaction(payuRequest);

            @SuppressWarnings("unchecked")
            Map<String, Object> txResponse = (Map<String, Object>) payuResponse.get("transactionResponse");

            // Imprimir la respuesta completa como JSON
            try {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(payuResponse);
                System.out.println("📥 Respuesta completa de PayU:\n" + json);
            } catch (Exception e) {
                System.out.println("❌ Error al serializar la respuesta de PayU a JSON: " + e.getMessage());
            }

            if (txResponse == null) {
                throw new PayuTransactionException("Error en respuesta de transacción desde PayU. Detalles: " + payuResponse);
            }
            String state = (String) txResponse.get("state");

            if ("APPROVED".equals(state)) {
                paymentDto.setConfirmation(1);
                paymentDto.setPaymentGatewayOrderId(String.valueOf(txResponse.get("orderId")));
                paymentDto.setPaymentGatewayTransactionId((String) txResponse.get("transactionId"));
                paymentDto.setCodeConfirmation((String) txResponse.get("authorizationCode"));
            } else if ("DECLINED".equals(state)) {
                paymentDto.setConfirmation(2);
            } else {
                throw new PayuTransactionException("Estado desconocido: " + state);
            }
        } else
            paymentDto.setCardNumber(null); // Esto para evitar guardar numero de tarjeta ingresados en front si es pago metodo sin tarjeta

        // 7. Guardar el pago
        Payment savedPayment = paymentMapper.toModel(
                paymentRepository.save(paymentMapper.toDao(paymentMapper.toModel(paymentDto)))
        );

        // 8. Asignar paymentId a órdenes procesadas
        orderList.forEach(order -> order.setPaymentId(savedPayment.getId()));
        orderProcessedService.createMultipleOrdersProcessed(orderList);

        // 9. Si fue rechazado, revertir stock y lanzar error
        if (paymentDto.getConfirmation() == 2) {
            updateStock(orderList, "increase");
            throw new PayuTransactionException("Pago rechazado por PayU");
        }

        // 10. Limpiar el carrito
        shoppingCartOrderService.deleteOrderByBuyerId(paymentDto.getBuyerId());

        return savedPayment;
    }

    public String confirmPaymenteById(Integer paymentId, Integer state) {

        PaymentDao dao = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Pago con ID: " + paymentId + " no encontrado"));

        if (dao.getConfirmation() != 0)
            throw new BadRequestException("Ese pago ya ha sido procesado (estado: " + dao.getConfirmation() + ")");

        if (state == 1) {
            dao.setConfirmation(1);
            dao.setCodeConfirmation(String.valueOf(ThreadLocalRandom.current().nextInt(1000, 9999)));
            paymentRepository.save(dao);
            return "Aprobado manualmente";
        } else if (state == 2) {
            dao.setConfirmation(2);
            paymentRepository.save(dao);

            List<OrderProcessedDto> orderList = orderProcessedService.getOrdersByPaymentId(paymentId).stream().map(orderProcessedMapper::toDto).toList();
            updateStock(orderList, "increase");

            return "Rechazado manualmente";
        } else {
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

    /*public Payment updatePayment(Integer id, PaymentDto updatedPaymentDto) {
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
    }*/

    private void updateStock(List<OrderProcessedDto> orders, String action) {
        if (!action.equals("increase") && !action.equals("decrease")) {
            throw new IllegalArgumentException("Acción inválida: " + action + ". Usa 'increase' o 'decrease'.");
        }

        for (OrderProcessedDto order : orders) {
            ProductDao producto = productRepository.findById(order.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

            int nuevoStock = switch (action) {
                case "increase" -> producto.getStock() + order.getUnits();
                case "decrease" -> {
                    int result = producto.getStock() - order.getUnits();
                    if (result < 0)
                        throw new BadRequestException("Stock insuficiente para producto ID: " + producto.getId());
                    yield result;
                }
                default -> throw new IllegalStateException("Acción no manejada: " + action);
            };

            producto.setStock(nuevoStock);
            productRepository.save(producto);
        }
    }

}
