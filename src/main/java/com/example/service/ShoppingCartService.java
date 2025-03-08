
/*package com.example.service;

import com.example.model.ShoppingCart;
import org.springframework.stereotype.Service;
import com.example.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public List<ShoppingCart> getAllShoppingCarts() {
        return shoppingCartRepository.findAll();
    }

    public Optional<ShoppingCart> getShoppingCartById(Integer id) {
        return shoppingCartRepository.findById(id);
    }

    public ShoppingCart createShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart);
    }

    public ShoppingCart updateShoppingCart(Integer id, ShoppingCart updatedShoppingCart) {
        return shoppingCartRepository.findById(id)
                .map(shoppingCart -> {

                    shoppingCart.setBuyerId(updatedShoppingCart.getBuyerId());
                    shoppingCart.setProductId(updatedShoppingCart.getProductId());
                    shoppingCart.setUnits(updatedShoppingCart.getUnits());
                    shoppingCart.setTotal(updatedShoppingCart.getTotal());
                    return shoppingCartRepository.save(shoppingCart);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public void deleteShoppingCart(Integer id) {
        shoppingCartRepository.deleteById(id);
    }
}
*/