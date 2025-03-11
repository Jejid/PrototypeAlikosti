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
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Error de argumento invalido: {}", ex.getMessage());
        return ResponseEntity.status(400).body("Solicitud inválida: " + ex.getMessage());
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
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {


        //Meter aquí en ifs las otras llaves que se pueden romper al crear o actulizar
        //de payment
        if (ex.getMessage().contains("payment_payment_method_id_fkey")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("el ID de metodo de pago especificado no existe.");
        } else
        if (ex.getMessage().contains("fk_payment_buyer")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("el ID de comprador especificado no existe.");
        }
        if (ex.getMessage().contains("check_paymentmethod_creditcard")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Si no es método tarjeta deja vacío el campo número de tarjeta. Si es método tarjeta debes llenar el campo número de tarjeta.");
        }

        //de product
        if (ex.getMessage().contains("product_product_category_id_fkey")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La categoría de producto especificada no existe.");
        }
        if (ex.getMessage().contains("fk_store")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("el ID de tienda especificada no existe.");
        }

        //de request_refund
        if (ex.getMessage().contains("fk_request_buyer")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("el ID de comprador especificado no existe.");
        }
        if (ex.getMessage().contains("fk_request_payment")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("el ID de pago especificado no existe.");
        }
        if (ex.getMessage().contains("unique_payment_id")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ya existe una solicitud de reembolso con este ID");
        }


        if (ex.getMessage().contains("el valor es demasiado largo para el tipo character varying")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Hay un campo tipo VarChar superando el límite de longitud, revísalo");
        }

        // Caso general para otras violaciones de integridad
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
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
            response.put("error", "No puedes solicitar reembolso porque el pago aún no se ha confirmado.");
        } else if (message.contains("Error: Pago rechazado")) {
            response.put("error", "No puedes solicitar reembolso porque el pago fue rechazado.");
        } else {
            response.put("error", "Error en la base de datos: " + message);
        }

        return new ResponseEntity<>(response, status);
    }

}

