package com.example.utility;

import com.example.dto.BuyerDto;
import com.example.dto.CreditCardDto;
import com.example.dto.PaymentDto;
import com.example.dto.payu.*;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class PayuRequestBuilder {

    private static final String API_KEY = "4Vj8eK4rloUd272L48hsrarnUA"; // sandbox
    private static final String API_LOGIN = "pRRXKOl8ikMmt9u";           // sandbox
    private static final String ACCOUNT_ID = "512321";                   // sandbox
    private static final String MERCHANT_ID = "508029";                  // sandbox
    private static final String CURRENCY = "COP";

    public static PayuPaymentRequest build(PaymentDto paymentDto, BuyerDto buyerDto, CreditCardDto creditCardDto) {
        PayuPaymentRequest request = new PayuPaymentRequest();
        request.setTest(true);
        request.setCommand("SUBMIT_TRANSACTION");
        request.setLanguage("es");

        // -------- MERCHANT ------------
        Merchant merchant = new Merchant();
        merchant.setApiKey(API_KEY);
        merchant.setApiLogin(API_LOGIN);
        request.setMerchant(merchant);

        // -------- ORDER ------------
        String referenceCode = "PRODUCT_TEST_" + System.currentTimeMillis();
        //String referenceCode = "PRODUCT_TEST_" + ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        BigDecimal value = new BigDecimal("65000.00");
        String formattedValue = String.format(Locale.US, "%.2f", value);
        String signature = generateSignature(API_KEY, MERCHANT_ID, referenceCode, formattedValue, CURRENCY);

        Order order = new Order();
        order.setAccountId(ACCOUNT_ID);
        order.setReferenceCode(referenceCode);
        order.setDescription("Payment test description");
        order.setLanguage("es");
        order.setSignature(signature);

        // ADDITIONAL VALUES (mínimo requerido)
        AdditionalValue additionalValue = new AdditionalValue();
        AdditionalValue.Amount txValue = new AdditionalValue.Amount();
        txValue.setValue(formattedValue);
        txValue.setCurrency(CURRENCY);
        additionalValue.setTX_VALUE(txValue);

        AdditionalValue.Amount txTax = new AdditionalValue.Amount();
        txTax.setValue("10378");
        txTax.setCurrency(CURRENCY);
        additionalValue.setTX_TAX(txTax);

        order.setAdditionalValues(additionalValue);

        // BUYER (mínimo requerido)
        Buyer buyer = new Buyer();
        buyer.setFullName("First name and second buyer name");
        buyer.setEmailAddress("buyer_test@test.com");
        buyer.setContactPhone("7563126");
        buyer.setDniNumber("123456789");

        ShippingAddress shipping = new ShippingAddress();
        shipping.setStreet1("Cr 23 No. 53-50");
        shipping.setCity("Bogota");
        shipping.setState("Bogota D.C.");
        shipping.setCountry("CO");
        shipping.setPostalCode("000000");
        shipping.setPhone("7563126");
        buyer.setShippingAddress(shipping);
        order.setBuyer(buyer);

        // -------- TRANSACTION ------------
        Transaction transaction = new Transaction();
        transaction.setOrder(order);

        // PAYER (mínimo requerido)
        Payer payer = new Payer();
        payer.setFullName("First name and second payer name");
        payer.setEmailAddress("payer_test@test.com");
        payer.setContactPhone("7563126");
        payer.setDniNumber("541566846");

        BillingAddress billing = new BillingAddress();
        billing.setStreet1("Cr 23 No. 53-50");
        billing.setCity("Bogota");
        billing.setCountry("CO");
        payer.setBillingAddress(billing);
        transaction.setPayer(payer);

        // CREDIT CARD
        CreditCard card = new CreditCard();
        card.setNumber("4037997623271984");
        card.setSecurityCode("321");
        card.setExpirationDate("2030/12");
        card.setName("APPROVED"); // importante para sandbox
        transaction.setCreditCard(card);

        // Otros campos mandatorios
        transaction.setType("AUTHORIZATION_AND_CAPTURE");
        transaction.setPaymentMethod("VISA");
        transaction.setPaymentCountry("CO");
        transaction.setDeviceSessionId("vghs6tvkcle931686k1900o6e1");
        transaction.setIpAddress("127.0.0.1");
        transaction.setCookie("pt1t38347bs6jc9ruv2ecpv7o2");
        transaction.setUserAgent("Mozilla/5.0 (Windows NT 5.1; rv:18.0) Gecko/20100101 Firefox/18.0");

        request.setTransaction(transaction);

        System.out.println("Referencia generada: " + referenceCode);
        System.out.println("Valor formateado: " + formattedValue);
        System.out.println("Firma generada: " + signature);

        return request;
    }

    private static String generateSignature(String apiKey, String merchantId, String referenceCode, String value, String currency) {
        BigDecimal amount = new BigDecimal(value);
        String formattedValue = String.format(Locale.US, "%.2f", amount);
        String raw = apiKey + "~" + merchantId + "~" + referenceCode + "~" + formattedValue + "~" + currency;
        return DigestUtils.md5DigestAsHex(raw.getBytes(StandardCharsets.UTF_8));
    }
}
