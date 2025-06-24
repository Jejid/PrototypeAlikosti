package com.example.utility;

import com.example.exception.RelatedEntityException;
import com.example.repository.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class DeletionValidator {

    @Autowired
    private ShoppingCartOrderRepository shoppingCartRepository;
    @Autowired
    private RequestRefundRepository requestRefundRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderProcessedRepository orderProcessedRepository;


    // Validación para buyer
    public void deletionValidatorBuyer(Integer buyerId) {
        if (shoppingCartRepository.existsByBuyerId(buyerId))
            throw new RelatedEntityException("No se puede eliminar el comprador porque tiene carrito de compras (orden) asociado.");
        if (requestRefundRepository.existsByBuyerId(buyerId))
            throw new RelatedEntityException("No se puede eliminar el comprador porque tiene un reembolso asociado.");
        if (paymentRepository.existsByBuyerId(buyerId))
            throw new RelatedEntityException("No se puede eliminar el comprador porque tiene un pago asociado.");
        // otros if para relación con otras tablas
    }

    // Validación para product
    public void deletionValidatorProduct(Integer productId) {
        if (shoppingCartRepository.existsByProductId(productId))
            throw new RelatedEntityException("No se puede eliminar el producto porque tiene carrito de compras (orden) asociado.");
        if (orderProcessedRepository.existsByProductId(productId))
            throw new RelatedEntityException("No se puede eliminar el producto porque tiene una orden procesada (venta) asociada.");
        // otros if para relación con otras tablas
    }

    // Validación para productCategory
    public void deletionValidatorProductCategory(Integer categoryId) {
        if (productRepository.existsByCategoryId(categoryId))
            throw new RelatedEntityException("No se puede eliminar la categoria porque tiene al menos un producto asociado.");
        // otros if para relación con otras tablas
    }

    // Validación para payment
    public void deletionValidatorPayment(Integer paymentId) {
        if (orderProcessedRepository.existsByPaymentId(paymentId))
            throw new RelatedEntityException("No se puede eliminar el pago porque tiene una orden procesada (venta) asociada.");
        // otros if para relación con otras tablas
    }

    // Validación para paymentMethod
    public void deletionValidatorPaymentMethod(Integer paymentMethodId) {
        if (paymentRepository.existsByPaymentMethodId(paymentMethodId))
            throw new RelatedEntityException("No se puede eliminar el método de pago porque tiene un pago asociado.");
        // otros if para relación con otras tablas
    }
}
