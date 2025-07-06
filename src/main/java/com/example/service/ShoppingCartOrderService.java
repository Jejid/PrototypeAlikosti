package com.example.service;

import com.example.dao.ShoppingCartOrderDao;
import com.example.dto.ShoppingCartOrderDto;
import com.example.exception.EntityNotFoundException;
import com.example.key.ShoppingCartOrderKey;
import com.example.mapper.ShoppingCartOrderMapper;
import com.example.model.Product;
import com.example.model.ShoppingCartOrder;
import com.example.repository.ShoppingCartOrderRepository;
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

        Product product = productService.getProductById(orderDto.getProductId());
        //verificamos que haya stock del producto
        if (product.getStock() == 0)
            throw new IllegalArgumentException("Este producto no tiene unidades en el stock en el momento");
        if (product.getStock() - orderDto.getUnits() < 0)
            throw new IllegalArgumentException("Este producto no tiene las unidades disponibles suficientes, prueba bajando el número");

        // verificamos que no exista el producto ya en el carrito
        if (orderRepository.existsById(new ShoppingCartOrderKey(orderDto.getBuyerId(), orderDto.getProductId())))
            throw new IllegalArgumentException("El item con buyerId " + orderDto.getBuyerId() + " y productId " + orderDto.getProductId() + " ya existe en el carrito");

        //seteamos verdadero total del producto, para evitar ediciones en front
        orderDto.setTotalProduct(orderDto.getUnits() * productService.getProductById(orderDto.getProductId()).getPrice());

        //convertimos, guardamos y regresamos el objeto itemOrder
        return itemOrderMapper.toModel(orderRepository.save(itemOrderMapper.toDao(itemOrderMapper.toModel(orderDto))));
    }

    public List<ShoppingCartOrder> getOrderByBuyerId(int buyerId) {

        buyerService.getBuyerById(buyerId);
        List<ShoppingCartOrder> userOrder = orderRepository
                .findAll()
                .stream()
                .filter(dao -> dao.getBuyerId() == buyerId)
                .map(itemOrderMapper::toModel)
                .toList();

        if (userOrder.isEmpty())
            throw new EntityNotFoundException("La Orden o Carrito del comprador con ID: " + buyerId + " está vacía");
        return userOrder;
    }

    public void createMultiplesItemOrder(List<ShoppingCartOrderDto> orderDto) {
        if (orderDto.isEmpty()) throw new IllegalArgumentException("La lista de items viene vacia.");
        orderDto.stream().map(dto -> itemOrderMapper.toDao(itemOrderMapper.toModel(dto))).forEach(orderRepository::save);
    }

    public ShoppingCartOrder updateItemOrder(int buyerId, int productId, ShoppingCartOrderDto orderDto) {

        Product product = productService.getProductById(productId);
        //verificamos que haya stock del producto
        if (product.getStock() == 0)
            throw new IllegalArgumentException("Este producto no tiene unidades en el stock en el momento");
        if (product.getStock() - orderDto.getUnits() < 0)
            throw new IllegalArgumentException("Este producto no tiene las unidades disponibles suficientes, prueba bajando el número");

        // verificamos que exista el producto en el carrito
        if (!orderRepository.existsById(new ShoppingCartOrderKey(buyerId, productId)))
            throw new IllegalArgumentException("Item de la orden con buyerId " + buyerId + " y productId " + productId + " no existe.");

        //seteamos verdadero total del producto, para evitar ediciones en front
        orderDto.setTotalProduct(orderDto.getUnits() * productService.getProductById(productId).getPrice());

        //seteamos los Ids del endpoint para evitar mal formaciones en el json
        orderDto.setProductId(productId);
        orderDto.setBuyerId(buyerId);

        return itemOrderMapper.toModel(orderRepository.save(itemOrderMapper.toDao(itemOrderMapper.toModel(orderDto))));
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
        buyerService.getBuyerById(buyerId); // verificamos que existe
        orderRepository
                .findAll()
                .stream()
                .filter(dao -> dao.getBuyerId() == buyerId)
                .map(dao -> new ShoppingCartOrderKey(dao.getBuyerId(), dao.getProductId()))
                .forEach(orderRepository::deleteById);
    }

    public ShoppingCartOrder partialUpdateItemOrder(int buyerId, int productId, Map<String, Object> updates) {

        Product product = productService.getProductById(productId);
        //verificamos que haya stock del producto
        if (product.getStock() == 0)
            throw new IllegalArgumentException("Este producto no tiene unidades en el stock en el momento");

        ShoppingCartOrderDao itemOrderDao = orderRepository.findById(new ShoppingCartOrderKey(buyerId, productId))
                .orElseThrow(() -> new EntityNotFoundException("Item de la orden con buyerId: " + buyerId + " productID: " + productId + " no existe"));


        if (updates.containsKey("units")) {
            if (product.getStock() - (Integer) updates.get("units") < 0)
                throw new IllegalArgumentException("Este producto no tiene las unidades disponibles suficientes, prueba bajando el número");
            if ((Integer) updates.get("units") <= 0)
                throw new IllegalArgumentException("El número de unidades no puede ser 0 o negativo");
            itemOrderDao.setUnits((Integer) updates.get("units"));
            itemOrderDao.setTotalProduct(itemOrderDao.getUnits() * productService.getProductById(productId).getPrice());
        }

        return itemOrderMapper.toModel(orderRepository.save(itemOrderDao));
    }
}
