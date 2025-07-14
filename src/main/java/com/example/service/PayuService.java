package com.example.service;

import com.example.dto.payu.PayuPaymentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class PayuService {

    private final RestTemplate restTemplate;

    private static final String PAYU_URL = "https://sandbox.api.payulatam.com/payments-api/4.0/service.cgi";

    public PayuService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> sendTransaction(PayuPaymentRequest request) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            System.out.println("📤 Enviando a PayU:\n" + json);
        } catch (Exception e) {
            System.out.println("❌ Error al serializar el request a JSON: " + e.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PayuPaymentRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                PAYU_URL,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );

        return response.getBody();
    }

}
