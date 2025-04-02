package com.one.digitalapi.service;


import com.one.digitalapi.entity.PaymentRequest;
import com.one.digitalapi.entity.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripePaymentService implements PaymentService {

  //  @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        Stripe.apiKey = stripeSecretKey;

        Map<String, Object> params = new HashMap<>();
        params.put("amount", request.getAmount() * 100); // Convert to cents
        params.put("currency", request.getCurrency());
        params.put("payment_method", request.getPaymentMethodId());
        params.put("confirm", true);

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return new PaymentResponse(paymentIntent.getId(), "Success", paymentIntent.getStatus());
        } catch (StripeException e) {
            return new PaymentResponse(null, "Failure", e.getMessage());
        }
    }
}