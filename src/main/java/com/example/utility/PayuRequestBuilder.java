package com.example.utility;

import com.example.dto.BuyerDto;
import com.example.dto.CreditCardDto;
import com.example.dto.PaymentDto;
import com.example.dto.payu.*;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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
        String referenceCode = "ORDER_" + UUID.randomUUID();
        String signature = generateSignature(API_KEY, MERCHANT_ID, referenceCode, String.valueOf(paymentDto.getTotalOrder()), CURRENCY);

        Order order = new Order();
        order.setAccountId(ACCOUNT_ID);
        order.setReferenceCode(referenceCode);
        order.setDescription("Pago de productos desde microservicio Alikosti");
        order.setSignature(signature);

        Buyer buyer = new Buyer();
        buyer.setFullName(buyerDto.getName());
        buyer.setEmailAddress(buyerDto.getEmail());
        order.setBuyer(buyer);

        AdditionalValue additionalValue = new AdditionalValue();
        AdditionalValue.Amount amount = new AdditionalValue.Amount();
        amount.setValue(String.valueOf(paymentDto.getTotalOrder()));
        amount.setCurrency(CURRENCY);
        additionalValue.setTX_VALUE(amount);
        order.setAdditionalValues(additionalValue);

        // -------- TRANSACTION ------------
        Transaction transaction = new Transaction();
        transaction.setOrder(order);

        Payer payer = new Payer();
        payer.setFullName(buyerDto.getName());
        payer.setEmailAddress(buyerDto.getEmail());
        transaction.setPayer(payer);

        CreditCard card = new CreditCard();
        card.setNumber(creditCardDto.getCardNumber());
        card.setSecurityCode(creditCardDto.getCvcCode());
        card.setExpirationDate(YearMonth.parse(creditCardDto.getCardDate(), DateTimeFormatter.ofPattern("MM/yy")).format(DateTimeFormatter.ofPattern("yyyy/MM")));
        card.setName(buyerDto.getName());
        transaction.setCreditCard(card);

        transaction.setPaymentMethod("VISA");
        transaction.setPaymentCountry("CO");

        // Datos dummy para test
        transaction.setDeviceSessionId("session123456");
        transaction.setIpAddress("127.0.0.1");
        transaction.setCookie("cookie123456");
        transaction.setUserAgent("Java-SpringBoot");

        request.setTransaction(transaction);

        return request;
    }

    private static String generateSignature(String apiKey, String merchantId, String referenceCode, String value, String currency) {
        String raw = apiKey + "~" + merchantId + "~" + referenceCode + "~" + value + "~" + currency;
        return DigestUtils.md5DigestAsHex(raw.getBytes(StandardCharsets.UTF_8));
    }
}
