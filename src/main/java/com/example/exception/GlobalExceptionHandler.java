package com.example.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PayGateTransactionException.class)
    public ResponseEntity<Map<String, Object>> handlePayGateTransactionException(PayGateTransactionException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.ZonedDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "PayGate Transaction Error");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Error de lógica de negocio");
        response.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFoundException(EntityNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Error de argumento inválido: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, String> response = new HashMap<>();

        String message = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();

        // Meter aquí en ifs las otras llaves que se pueden romper al crear o actualizar

        // de payment
        if (message.contains("payment_payment_method_id_fkey")) {
            response.put("error", "el ID de metodo de pago especificado no existe.");
        } else if (message.contains("fk_payment_buyer")) {
            response.put("error", "el ID de comprador especificado no existe.");
        } else if (message.contains("check_code_confirmation_not_null")) {
            response.put("error", "Al aprobarse el pago confirmation=1, el código de confirmación debe agregarse, no puede ser nulo");

            // de product
        } else if (message.contains("product_product_category_id_fkey")) {
            response.put("error", "La categoría de producto especificada no existe.");
        } else if (message.contains("fk_store")) {
            response.put("error", "el ID de tienda especificada no existe.");

            // de request_refund
        } else if (message.contains("fk_request_buyer")) {
            response.put("error", "el ID de comprador especificado no existe.");
        } else if (message.contains("fk_request_payment")) {
            if (message.toLowerCase().contains("delete")) {
                response.put("error", "No puedes eliminar el pago porque tiene solicitudes de reembolso asociadas.");
            } else {
                response.put("error", "el ID de pago especificado no existe.");
            }
        } else if (message.contains("unique_payment_id")) {
            response.put("error", "Ya existe una solicitud de reembolso con este ID de pago");

            // de product_category
        } else if (message.contains("fk_store_pcategory")) {
            response.put("error", "el ID de tienda especificado no existe.");

            // de credit_card
        } else if (message.contains("idx_unique_card_for_buyer")) {
            response.put("error", "Ya hay una tarjeta tokenizada para ese comprador, usa su toke o ingresa una nueva tarjeta");
        } else if (message.contains("fk_buyer_creditcard")) {
            response.put("error", "el ID de comprador especificado no existe.");

            // de shoppingcart_order
        } else if (message.contains("fk_buyer_shoppingcart")) {
            response.put("error", "el ID de comprador especificado no existe.");
        } else if (message.contains("fk_product_shoppingcart")) {
            response.put("error", "el ID de producto especificado no existe.");
        } else if (message.contains("unique_item_shoppingcart")) {
            response.put("error", "Ya existe una entrada a la Orden de comprador y producto con esa combinación");

            // caso de un campo largo
        } else if (message.contains("el valor es demasiado largo para el tipo character varying")) {
            response.put("error", "Hay un campo tipo VarChar superando el límite de longitud, revísalo");
        } else {

            // Caso general para otras violaciones de integridad
            response.put("error", message);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(BadRequestException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonParseError(HttpMessageNotReadableException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "El cuerpo de la solicitud contiene un JSON malformado o inválido.");
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException ex) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // Extraer el mensaje del error SQL
        String message = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();

        // Verificar si el mensaje contiene el error del trigger
        if (message.contains("Error: Pago No confirmado")) {
            response.put("error", "No puedes solicitar reembolso porque el pago aún no se ha confirmado o no existe dicho pago.");
        } else if (message.contains("Error: Pago rechazado")) {
            response.put("error", "No puedes solicitar reembolso porque el pago fue rechazado.");
        } else {
            response.put("error", "Error en la base de datos: " + message);
        }

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNoHandlerFound(NoHandlerFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", 404);
        error.put("error", "Ruta no encontrada");
        error.put("message", "La URL '" + ex.getRequestURL() + "' no existe o está mal escrita. Verifica si escribiste mal el endpoint.");
        return error;
    }

    //Excepción para manejar relaciones con otra entidad en especial en el tema de deletion
    @ExceptionHandler(RelatedEntityException.class)
    public ResponseEntity<Map<String, String>> handleRelatedEntityException(RelatedEntityException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // 409
    }

    @ExceptionHandler(com.example.exception.InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentialsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED); // 401
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND); // 404
    }
}

