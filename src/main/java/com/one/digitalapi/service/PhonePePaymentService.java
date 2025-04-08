package com.one.digitalapi.service;

import com.one.digitalapi.entity.PaymentRequest;
import com.one.digitalapi.entity.PaymentResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service("phonepePaymentService")
public class PhonePePaymentService implements PaymentService {

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        // Simulate PhonePe logic (can be replaced with real API)

        // Simulate transaction ID
        String txnId = "PHONEPE-" + UUID.randomUUID();

        // Simulate current timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Simulate a dummy payment success message
        String message = String.format("PhonePe Payment of ₹%.2f %s processed at %s",
                request.getAmount(), request.getCurrency(), timestamp);

        return new PaymentResponse(txnId, "Success", message);
    }
}
