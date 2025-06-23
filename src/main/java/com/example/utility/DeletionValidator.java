package com.example.utility;

import com.example.exception.RelatedEntityException;
import com.example.repository.PaymentRepository;
import com.example.repository.RequestRefundRepository;
import com.example.repository.ShoppingCartOrderRepository;
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


    // Validación para buyer
    public void deletionValidatorBuyer(Integer buyerId) {
        if (shoppingCartRepository.existsByBuyerId(buyerId))
            throw new RelatedEntityException("No se puede eliminar el comprador porque tiene carrito de compras asociado.");
        if (requestRefundRepository.existsByBuyerId(buyerId))
            throw new RelatedEntityException("No se puede eliminar el comprador porque tiene un reembolso asociado.");
        if (paymentRepository.existsByBuyerId(buyerId))
            throw new RelatedEntityException("No se puede eliminar el comprador porque tiene un pago asociado.");
        // otros if para relación con otras tablas
    }

    // Validación para producto
}
