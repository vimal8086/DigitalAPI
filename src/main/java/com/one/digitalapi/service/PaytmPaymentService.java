package com.one.digitalapi.service;

import com.one.digitalapi.entity.PaymentRequest;
import com.one.digitalapi.entity.PaymentResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service("paytmPaymentService")
public class PaytmPaymentService implements PaymentService {

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        // Simulate Paytm logic (can be replaced with real API integration)

        // Generate a mock transaction ID
        String txnId = "PAYTM-" + UUID.randomUUID();

        // Format current timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Construct a simulated success message
        String message = String.format("Paytm Payment of ₹%.2f %s completed successfully at %s",
                request.getAmount(), request.getCurrency(), timestamp);

        return new PaymentResponse(txnId, "Success", message);
    }
}
