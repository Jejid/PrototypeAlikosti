package com.example.utility;

import com.example.dto.BuyerDto;
import com.example.dto.CreditCardDto;
import com.example.dto.PaymentDto;
import com.example.dto.payu.*;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class PayuRequestBuilder {

    private static final String API_KEY = "4Vj8eK4rloUd272L48hsrarnUA"; // sandbox
    private static final String API_LOGIN = "pRRXKOl8ikMmt9u";           // sandbox
    private static final String ACCOUNT_ID = "512321";                   // sandbox
    private static final String MERCHANT_ID = "508029";                  // sandbox
    private static final String CURRENCY = "COP";

    // ✅ Funcion para construir solicitud de pago
    public static PayuPaymentRequest buildPayment(PaymentDto paymentDto, BuyerDto buyerDto, CreditCardDto creditCardDto) {
        PayuPaymentRequest request = new PayuPaymentRequest();
        request.setTest(true);
        request.setCommand("SUBMIT_TRANSACTION");
        request.setLanguage("es");

        Merchant merchant = new Merchant();
        merchant.setApiKey(API_KEY);
        merchant.setApiLogin(API_LOGIN);
        request.setMerchant(merchant);

        String referenceCode = "PRODUCT_TEST_" + System.currentTimeMillis();
        String value = String.valueOf(paymentDto.getTotalOrder());
        String signature = generateSignature(API_KEY, MERCHANT_ID, referenceCode, value, CURRENCY);

        Order order = new Order();
        order.setAccountId(ACCOUNT_ID);
        order.setReferenceCode(referenceCode);
        order.setDescription("Payment test description");
        order.setLanguage("es");
        order.setSignature(signature);

        AdditionalValue additionalValue = new AdditionalValue();
        AdditionalValue.Amount txValue = new AdditionalValue.Amount();
        txValue.setValue(value);
        txValue.setCurrency(CURRENCY);
        additionalValue.setTX_VALUE(txValue);

        AdditionalValue.Amount txTax = new AdditionalValue.Amount();
        txTax.setValue(String.valueOf(Integer.parseInt(value) * 0.19));
        txTax.setCurrency(CURRENCY);
        additionalValue.setTX_TAX(txTax);

        order.setAdditionalValues(additionalValue);

        Buyer buyer = new Buyer();
        buyer.setFullName(buyerDto.getName());
        buyer.setEmailAddress(buyerDto.getEmail());
        buyer.setContactPhone("7563126");
        buyer.setDniNumber(buyerDto.getCc());

        ShippingAddress shipping = new ShippingAddress();
        shipping.setStreet1("Cr 23 No. 53-50");
        shipping.setCity("Bogotá");
        shipping.setState("Bogotá D.C.");
        shipping.setCountry("CO");
        shipping.setPostalCode("000000");
        shipping.setPhone("7563126");
        buyer.setShippingAddress(shipping);
        order.setBuyer(buyer);

        Transaction transaction = new Transaction();
        transaction.setOrder(order);

        Payer payer = new Payer();
        payer.setFullName(buyerDto.getName());
        payer.setEmailAddress(buyerDto.getEmail());
        payer.setContactPhone("7563126");
        payer.setDniNumber(buyerDto.getCc());

        BillingAddress billing = new BillingAddress();
        billing.setStreet1("Cr 23 No. 53-50");
        billing.setCity("Bogotá");
        billing.setCountry("CO");
        payer.setBillingAddress(billing);
        transaction.setPayer(payer);

        CreditCard card = new CreditCard();
        card.setNumber(creditCardDto.getCardNumber());
        card.setSecurityCode(creditCardDto.getCvcCode());
        card.setExpirationDate(YearMonth.parse(creditCardDto.getCardDate(), DateTimeFormatter.ofPattern("MM/yy"))
                .format(DateTimeFormatter.ofPattern("yyyy/MM")));
        card.setName("APPROVED");
        transaction.setCreditCard(card);

        transaction.setType("AUTHORIZATION_AND_CAPTURE");
        transaction.setPaymentMethod(creditCardDto.getFranchise());
        transaction.setPaymentCountry("CO");
        transaction.setDeviceSessionId("vghs6tvkcle931686k1900o6e1");
        transaction.setIpAddress("127.0.0.1");
        transaction.setCookie("pt1t38347bs6jc9ruv2ecpv7o2");
        transaction.setUserAgent("Mozilla/5.0 (Windows NT 5.1; rv:18.0) Gecko/20100101 Firefox/18.0");

        request.setTransaction(transaction);
        return request;
    }

    // ✅ Funcion para construir solicitud de reembolso
    public static PayuRefundRequest buildRefund(String orderId, String parentTransactionId, String reason, Double partialAmount) {
        PayuRefundRequest request = new PayuRefundRequest();
        request.setTest(true);
        request.setLanguage("es");
        request.setCommand("SUBMIT_TRANSACTION");

        Merchant merchant = new Merchant();
        merchant.setApiKey(API_KEY);
        merchant.setApiLogin(API_LOGIN);
        request.setMerchant(merchant);

        Order order = new Order();
        order.setId(orderId);

        RefundTransaction refundTransaction = RefundTransaction.builder()
                .order(order)
                .type(partialAmount != null ? "PARTIAL_REFUND" : "REFUND")
                .parentTransactionId(parentTransactionId)
                .reason(reason)
                .build();

        if (partialAmount != null) {
            AdditionalValue additionalValue = new AdditionalValue();
            AdditionalValue.Amount txValue = new AdditionalValue.Amount();
            txValue.setValue(String.valueOf(partialAmount));
            txValue.setCurrency("COP");
            additionalValue.setTX_VALUE(txValue);
            refundTransaction.setAdditionalValues(additionalValue);
        }

        request.setTransaction(refundTransaction);
        return request;
    }

    // funcion para generar la firma de la solicitud
    private static String generateSignature(String apiKey, String merchantId, String referenceCode, String value, String currency) {
        String raw = apiKey + "~" + merchantId + "~" + referenceCode + "~" + value + "~" + currency;
        return DigestUtils.md5DigestAsHex(raw.getBytes(StandardCharsets.UTF_8));
    }
}
