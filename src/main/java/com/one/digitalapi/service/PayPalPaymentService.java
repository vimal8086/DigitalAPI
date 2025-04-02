package com.one.digitalapi.service;

import com.one.digitalapi.entity.PaymentRequest;
import com.one.digitalapi.entity.PaymentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PayPalPaymentService implements PaymentService {

   // @Value("${paypal.client.id}")
    private String clientId;

   // @Value("${paypal.secret}")
    private String secret;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        // Implement PayPal API Integration Here
        return new PaymentResponse("PAYPAL_TXN_ID", "Success", "Transaction Completed");
    }
}