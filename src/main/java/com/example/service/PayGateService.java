package com.example.service;

import com.example.dto.paygate.PayGatePaymentRequest;
import com.example.dto.paygate.PayGateRefundRequest;
import com.example.dto.paygate.PayGateTokenRequest;
import com.example.exception.PayGateTransactionException;
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
public class PayGateService {

    private final RestTemplate restTemplate;

    private static final String PAYGATE_URL = "https://payGate.com/payments";

    public PayGateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> sendPaymentTransaction(PayGatePaymentRequest request) {

        try {
            // 🔍 Imprimir JSON saliente (opcional)
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            System.out.println("📤 Enviando a PayGate:\n" + json);
        } catch (Exception e) {
            throw new PayGateTransactionException("Error al serializar el request a JSON", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PayGatePaymentRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    PAYGATE_URL,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> body = response.getBody();
                if (body == null || !body.containsKey("transactionResponse")) {
                    throw new PayGateTransactionException("Respuesta incompleta desde PayGate: " + body);
                }
                return body;
            } else {
                throw new PayGateTransactionException("PayGate respondió con estado HTTP no exitoso: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // ⚠️ Error con cuerpo de respuesta HTTP
            throw new PayGateTransactionException("Error HTTP desde PayGate " + ex.getStatusCode() +
                    " - " + ex.getResponseBodyAsString(), ex);

        } catch (ResourceAccessException ex) {
            // 🚫 Error de conexión
            throw new PayGateTransactionException("No se pudo conectar a PayGate. Posible caída del servicio o error de red.", ex);

        } catch (Exception ex) {
            // 🔥 Error inesperado
            throw new PayGateTransactionException("Error inesperado al procesar la transacción con PayGate", ex);
        }
    }

    public Map<String, Object> sendRefundTransaction(PayGateRefundRequest request) {

        try {
            // 🔍 Imprimir JSON saliente (opcional)
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            System.out.println("📤 Enviando solicitud de reembolso a PayGate:\n" + json);
        } catch (Exception e) {
            throw new PayGateTransactionException("Error al serializar el request de reembolso a JSON", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PayGateRefundRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    PAYGATE_URL,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> body = response.getBody();
                if (body == null || !body.containsKey("transactionResponse")) {
                    throw new PayGateTransactionException("Respuesta incompleta desde PayGate en reembolso: " + body);
                }
                return body;
            } else {
                throw new PayGateTransactionException("PayGate respondió con estado HTTP no exitoso: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new PayGateTransactionException("Error HTTP desde PayGate (reembolso): " + ex.getStatusCode() +
                    " - " + ex.getResponseBodyAsString(), ex);

        } catch (ResourceAccessException ex) {
            throw new PayGateTransactionException("No se pudo conectar a PayGate. Posible caída del servicio o error de red (reembolso).", ex);

        } catch (Exception ex) {
            throw new PayGateTransactionException("Error inesperado al procesar el reembolso con PayGate", ex);
        }
    }

    public Map<String, Object> sendTokenRequest(PayGateTokenRequest request) {

        try {
            // 🔍 Imprimir JSON saliente (opcional, útil para pruebas)
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            System.out.println("📤 Enviando solicitud de tokenización a PayGate:\n" + json);
        } catch (Exception e) {
            throw new PayGateTransactionException("Error al serializar el request de tokenización a JSON", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PayGateTokenRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    PAYGATE_URL,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> body = response.getBody();
                if (body == null || !body.containsKey("creditCardToken")) {
                    throw new PayGateTransactionException("Respuesta incompleta desde PayGate (tokenización): " + body);
                }
                return body;
            } else {
                throw new PayGateTransactionException("PayGate respondió con estado HTTP no exitoso (tokenización): " + response.getStatusCode());
            }

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new PayGateTransactionException("Error HTTP desde PayGate (tokenización): " + ex.getStatusCode() +
                    " - " + ex.getResponseBodyAsString(), ex);

        } catch (ResourceAccessException ex) {
            throw new PayGateTransactionException("No se pudo conectar a PayGate. Posible caída del servicio o error de red (tokenización).", ex);

        } catch (Exception ex) {
            throw new PayGateTransactionException("Error inesperado al procesar la tokenización con PayGate", ex);
        }
    }
}
