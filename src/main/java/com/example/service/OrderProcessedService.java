package com.example.service;

import com.example.dao.OrderProcessedDao;
import com.example.dto.OrderProcessedDto;
import com.example.exception.EntityNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.key.OrderProcessedKey;
import com.example.mapper.OrderProcessedMapper;
import com.example.model.OrderProcessed;
import com.example.repository.OrderProcessedRepository;
import com.example.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderProcessedService {

    private final OrderProcessedRepository orderProcessedRepository;
    private final ProductService productService;
    private final OrderProcessedMapper orderProcessedMapper;
    private final PaymentRepository paymentRepository;


    public List<OrderProcessed> getSalesByBuyerId(int id) {
        if (orderProcessedRepository.findAllByBuyerId(id).isEmpty())
            throw new UserNotFoundException("Es comprador de id: " + id + " no tiene compras registradas");
        return orderProcessedRepository.findAllByBuyerId(id).stream().map(orderProcessedMapper::toModel).toList();
    }

    //---- Metodos Basicos ----
    public OrderProcessedService(OrderProcessedRepository orderProcessedRepository, ProductService productService, OrderProcessedMapper orderProcessedMapper, PaymentRepository paymentRepository) {
        this.orderProcessedRepository = orderProcessedRepository;
        this.productService = productService;
        this.orderProcessedMapper = orderProcessedMapper;
        this.paymentRepository = paymentRepository;
    }

    public List<OrderProcessed> getAllOrdersProcessed() {
        List<OrderProcessedDao> daoList = orderProcessedRepository.findAll();
        return daoList.stream().map(orderProcessedMapper::toModel).collect(Collectors.toList());
    }

    public OrderProcessed getOrderProcessedById(int paymentId, int productId) {
        OrderProcessedDao dao = orderProcessedRepository.findById(new OrderProcessedKey(paymentId, productId))
                .orElseThrow(() -> new EntityNotFoundException("Orden procesada con paymentId: " + paymentId + " y productId: " + productId + " no existe"));
        return orderProcessedMapper.toModel(dao);
    }

    public OrderProcessed createItemOrderProcessed(OrderProcessedDto dto) {
        if (orderProcessedRepository.existsById(new OrderProcessedKey(dto.getPaymentId(), dto.getProductId())))
            throw new IllegalArgumentException("La orden ya existe con paymentId: " + dto.getPaymentId() + " y productId: " + dto.getProductId());

        dto.setTotalProduct(dto.getUnits() * productService.getProductById(dto.getProductId()).getPrice());

        return orderProcessedMapper.toModel(orderProcessedRepository.save(orderProcessedMapper.toDao(orderProcessedMapper.toModel(dto))));
    }

    public List<OrderProcessed> getOrdersByPaymentId(int paymentId) {
        if (!paymentRepository.existsById(paymentId)) {
            throw new EntityNotFoundException("No existe un pago con ID " + paymentId);
        }
        //paymentService.getPaymentById(paymentId);
        List<OrderProcessed> orders = orderProcessedRepository.findAll().stream()
                .filter(dao -> dao.getPaymentId() == paymentId)
                .map(orderProcessedMapper::toModel)
                .collect(Collectors.toList());

        if (orders.isEmpty())
            throw new EntityNotFoundException("No hay órdenes procesadas para el paymentId: " + paymentId);

        return orders;
    }

    public void createMultipleOrdersProcessed(List<OrderProcessedDto> dtos) {
        if (dtos.isEmpty()) throw new IllegalArgumentException("La lista de órdenes está vacía.");
        dtos.stream()
                .map(dto -> orderProcessedMapper.toDao(orderProcessedMapper.toModel(dto)))
                .forEach(orderProcessedRepository::save);
    }

    public OrderProcessed updateOrderProcessed(int paymentId, int productId, OrderProcessedDto dto) {
        if (!orderProcessedRepository.existsById(new OrderProcessedKey(paymentId, productId)))
            throw new IllegalArgumentException("Orden con paymentId: " + paymentId + " y productId: " + productId + " no existe.");

        dto.setTotalProduct(dto.getUnits() * productService.getProductById(productId).getPrice());
        dto.setPaymentId(paymentId);
        dto.setProductId(productId);

        return orderProcessedMapper.toModel(orderProcessedRepository.save(orderProcessedMapper.toDao(orderProcessedMapper.toModel(dto))));
    }

    public void deleteOrderProcessed(int paymentId, int productId) {
        OrderProcessedKey key = new OrderProcessedKey(paymentId, productId);
        if (!orderProcessedRepository.existsById(key)) {
            throw new EntityNotFoundException("Orden procesada con paymentId: " + paymentId + " y productId: " + productId + " no existe");
        }
        orderProcessedRepository.deleteById(key);
    }

    public void deleteOrdersByPaymentId(int paymentId) {
        if (!paymentRepository.existsById(paymentId)) {
            throw new EntityNotFoundException("No existe un pago con ID " + paymentId);
        }
        //paymentService.getPaymentById(paymentId);
        orderProcessedRepository.findAll().stream()
                .filter(dao -> dao.getPaymentId() == paymentId)
                .map(dao -> new OrderProcessedKey(dao.getPaymentId(), dao.getProductId()))
                .forEach(orderProcessedRepository::deleteById);
    }

    public OrderProcessed partialUpdateOrderProcessed(int paymentId, int productId, Map<String, Object> updates) {
        OrderProcessedDao dao = orderProcessedRepository.findById(new OrderProcessedKey(paymentId, productId))
                .orElseThrow(() -> new EntityNotFoundException("Orden procesada con paymentId: " + paymentId + " y productId: " + productId + " no existe"));

        if (updates.containsKey("units")) {
            dao.setUnits((Integer) updates.get("units"));
            dao.setTotalProduct(dao.getUnits() * productService.getProductById(productId).getPrice());
        }

        return orderProcessedMapper.toModel(orderProcessedRepository.save(dao));
    }
}
