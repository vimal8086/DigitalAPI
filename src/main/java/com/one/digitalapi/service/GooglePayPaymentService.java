package com.one.digitalapi.service;

import com.one.digitalapi.entity.PaymentRequest;
import com.one.digitalapi.entity.PaymentResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service("googlepayPaymentService")
public class GooglePayPaymentService implements PaymentService {

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        // Simulate Google Pay logic (can be replaced with UPI API integration)

        // Generate a mock transaction ID
        String txnId = "GOOGLEPAY-" + UUID.randomUUID();

        // Get formatted timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Simulated success message
        String message = String.format("Google Pay Payment of ₹%.2f %s processed successfully at %s",
                request.getAmount(), request.getCurrency(), timestamp);

        return new PaymentResponse(txnId, "Success", message);
    }
}
