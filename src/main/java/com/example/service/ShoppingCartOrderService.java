package com.example.service;

import com.example.dao.ShoppingCartOrderDao;
import com.example.dto.ShoppingCartOrderDto;
import com.example.exception.EntityNotFoundException;
import com.example.key.ShoppingCartOrderKey;
import com.example.model.ShoppingCartOrder;
import com.example.repository.ShoppingCartOrderRepository;
import com.example.utility.ShoppingCartOrderMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShoppingCartOrderService {

    private final ShoppingCartOrderRepository orderRepository;
    private final ProductService productService;
    private final ShoppingCartOrderMapper itemOrderMapper;
    private final BuyerService buyerService;

    public ShoppingCartOrderService(ShoppingCartOrderRepository orderRepository, ProductService productService, ShoppingCartOrderMapper itemOrderMapper, BuyerService buyerService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.itemOrderMapper = itemOrderMapper;
        this.buyerService = buyerService;
    }

    public List<ShoppingCartOrder> getAllItemOrders() {
        List<ShoppingCartOrderDao> daoList = orderRepository.findAll();
        return daoList.stream().map(itemOrderMapper::toModel).collect(Collectors.toList());
    }

    public ShoppingCartOrder getItemOrderById(int buyerId, int productId) {
        ShoppingCartOrderDao dao = orderRepository.findById(new ShoppingCartOrderKey(buyerId, productId))
                .orElseThrow(() -> new EntityNotFoundException("Item de la orden con buyerId: " + buyerId + " productID: " + productId + " no existe"));
        return itemOrderMapper.toModel(dao);
    }

    public ShoppingCartOrder createItemOrder(ShoppingCartOrderDto orderDto) {

        // verificamos que no exista el producto ya en el carrito
        if (orderRepository.existsById(new ShoppingCartOrderKey(orderDto.getBuyerId(), orderDto.getProductId())))
            throw new IllegalArgumentException("El item con buyerId " + orderDto.getBuyerId() + " y productId " + orderDto.getProductId() + " ya existe.");

        //seteamos verdadero total del producto, para evitar ediciones en front
        orderDto.setTotal_product(orderDto.getUnits() * productService.getProductById(orderDto.getProductId()).getPrice());

        //convertimos, guardamos y regresamos el objeto itemOrder
        return itemOrderMapper.toModel(orderRepository.save(itemOrderMapper.toDao(itemOrderMapper.toModel(orderDto))));
    }

    public List<ShoppingCartOrder> getOrderByBuyerId(int buyerId) {
        /*List<ShoppingCartOrderDao> daoList = orderRepository.findAll();
        List<ShoppingCartOrder> userOrder = new ArrayList<>();
        for (ShoppingCartOrderDao dao : daoList) {
            if (dao.getBuyerId() == buyerId) userOrder.add(toModel(dao));
        }*/

        List<ShoppingCartOrder> userOrder = orderRepository.findAll().stream().filter(dao -> dao.getBuyerId() == buyerId).map(itemOrderMapper::toModel).collect(Collectors.toList());

        if (userOrder.isEmpty())
            throw new EntityNotFoundException("La Orden o Carrito del comprador con ID: " + buyerId + " está vacía o no existe el comprador");
        return userOrder;
    }

    public void createTotalOrder(List<ShoppingCartOrder> orders) {
        if (orders.isEmpty()) throw new IllegalArgumentException("La lista de items viene vacia.");

        for (ShoppingCartOrder order : orders) {
            ShoppingCartOrderDao dao = toDao(order);
            orderRepository.save(dao);
        }
    }

    public ShoppingCartOrder updateItemOrder(int buyerId, int productId, ShoppingCartOrder order) {
        ShoppingCartOrderKey key = new ShoppingCartOrderKey(buyerId, productId);
        ShoppingCartOrderDao existingDao = orderRepository.findById(key)
                .orElseThrow(() -> new EntityNotFoundException("Item de la orden con buyerId: " + buyerId + " productID: " + productId + " no existe"));

        existingDao.setUnits(order.getUnits());
        existingDao.setTotal_product(order.getTotal_product());

        ShoppingCartOrderDao saved = orderRepository.save(existingDao);
        return toModel(saved);
    }

    public void deleteItemOrder(int buyerId, int productId) {
        ShoppingCartOrderKey key = new ShoppingCartOrderKey(buyerId, productId);
        if (!orderRepository.existsById(key)) {
            throw new EntityNotFoundException("Item de la orden con buyerId: " + buyerId + " productID: " + productId + " no existe");
        }
        orderRepository.deleteById(key);
    }

    //Borrar el carrito de compras para ese comprador
    public void deleteOrderByBuyerId(int buyerId) {
        buyerService.getBuyerById(buyerId);
        orderRepository.findAll().stream().filter(dao -> dao.getBuyerId() == buyerId).map(dao -> new ShoppingCartOrderKey(dao.getBuyerId(), dao.getProductId())).forEach(orderRepository::deleteById);
        /*for (ShoppingCartOrder itemOrder : buyerOrder) {
            ShoppingCartOrderKey key = new ShoppingCartOrderKey(itemOrder.getBuyerId(), itemOrder.getProductId());
            repository.deleteById(key);
        }*/
    }

    public ShoppingCartOrder partialUpdateItemOrder(int buyerId, int productId, Map<String, Object> updates) {
        ShoppingCartOrderKey key = new ShoppingCartOrderKey(buyerId, productId);
        ShoppingCartOrderDao existingDao = orderRepository.findById(key)
                .orElseThrow(() -> new EntityNotFoundException("Item de la orden con buyerId: " + buyerId + " productID: " + productId + " no existe"));

        if (updates.containsKey("units")) {
            existingDao.setUnits((Integer) updates.get("units"));
        }
        if (updates.containsKey("total_product")) {
            existingDao.setTotal_product((Integer) updates.get("total_product"));
        }

        ShoppingCartOrderDao saved = orderRepository.save(existingDao);
        return toModel(saved);
    }

    // Conversiones DAO -> Model
    private ShoppingCartOrder toModel(ShoppingCartOrderDao dao) {
        ShoppingCartOrder model = new ShoppingCartOrder();
        model.setBuyerId(dao.getBuyerId());
        model.setProductId(dao.getProductId());
        model.setUnits(dao.getUnits());
        model.setTotal_product(dao.getTotal_product());
        return model;
    }

    // Conversiones Model -> DAO
    private ShoppingCartOrderDao toDao(ShoppingCartOrder model) {
        ShoppingCartOrderDao dao = new ShoppingCartOrderDao();
        dao.setBuyerId(model.getBuyerId());
        dao.setProductId(model.getProductId());
        dao.setUnits(model.getUnits());
        dao.setTotal_product(model.getTotal_product());
        return dao;
    }
}
