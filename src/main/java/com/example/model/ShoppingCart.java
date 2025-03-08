/*package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "shoppingcart_order") // Asegurar que coincide con la tabla en PostgreSQL
public class ShoppingCart {

    @NotNull(message = "El ID del comprador es necesario")
    private int buyerId;
    @NotNull(message = "El ID del comprador es necesario")
    private int productId;
    @NotNull(message = "El campo unidades no puede estar vacío")
    @Min(value = 0, message = "La unidades deben ser mayor o igual a 0")
    private int units;
    @NotNull(message = "El total no puede estar vacío")
    @Min(value = 0, message = "El total debe ser mayor o igual a 0")
    private int total;
}
*/