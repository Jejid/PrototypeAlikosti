package com.example.service;

import com.example.dto.payu.PayuPaymentRequest;
import com.example.dto.payu.PayuRefundRequest;
import com.example.exception.PayuTransactionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class PayuService {

    private final RestTemplate restTemplate;

    private static final String PAYU_URL = "https://sandbox.api.payulatam.com/payments-api/4.0/service.cgi";

    public PayuService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> sendPaymentTransaction(PayuPaymentRequest request) {

        try {
            // 🔍 Imprimir JSON saliente (opcional)
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            System.out.println("📤 Enviando a PayU:\n" + json);
        } catch (Exception e) {
            throw new PayuTransactionException("Error al serializar el request a JSON", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PayuPaymentRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    PAYU_URL,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> body = response.getBody();
                if (body == null || !body.containsKey("transactionResponse")) {
                    throw new PayuTransactionException("Respuesta incompleta desde PayU: " + body);
                }
                return body;
            } else {
                throw new PayuTransactionException("PayU respondió con estado HTTP no exitoso: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // ⚠️ Error con cuerpo de respuesta HTTP
            throw new PayuTransactionException("Error HTTP desde PayU: " + ex.getStatusCode() +
                    " - " + ex.getResponseBodyAsString(), ex);

        } catch (ResourceAccessException ex) {
            // 🚫 Error de conexión
            throw new PayuTransactionException("No se pudo conectar a PayU. Posible caída del servicio o error de red.", ex);

        } catch (Exception ex) {
            // 🔥 Error inesperado
            throw new PayuTransactionException("Error inesperado al procesar la transacción con PayU", ex);
        }
    }

    public Map<String, Object> sendRefundTransaction(PayuRefundRequest request) {

        try {
            // 🔍 Imprimir JSON saliente (opcional)
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            System.out.println("📤 Enviando solicitud de reembolso a PayU:\n" + json);
        } catch (Exception e) {
            throw new PayuTransactionException("Error al serializar el request de reembolso a JSON", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PayuRefundRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    PAYU_URL,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> body = response.getBody();
                if (body == null || !body.containsKey("transactionResponse")) {
                    throw new PayuTransactionException("Respuesta incompleta desde PayU en reembolso: " + body);
                }
                return body;
            } else {
                throw new PayuTransactionException("PayU respondió con estado HTTP no exitoso: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new PayuTransactionException("Error HTTP desde PayU (reembolso): " + ex.getStatusCode() +
                    " - " + ex.getResponseBodyAsString(), ex);

        } catch (ResourceAccessException ex) {
            throw new PayuTransactionException("No se pudo conectar a PayU. Posible caída del servicio o error de red (reembolso).", ex);

        } catch (Exception ex) {
            throw new PayuTransactionException("Error inesperado al procesar el reembolso con PayU", ex);
        }
    }


}
