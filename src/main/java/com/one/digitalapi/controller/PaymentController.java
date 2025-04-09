package com.one.digitalapi.controller;

import com.one.digitalapi.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/payment")
@Tag(name = "Payment Management", description = "APIs for managing payment")
public class PaymentController {

    public PaymentController() {
    }

    @Autowired
    private PaymentService paymentService;
    @GetMapping("/createOrder")
    public String createOrder(@RequestParam double amount, @RequestParam String currency) {
        try {
            return paymentService.createOrder(amount, currency);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/verify")
    public ResponseEntity verifyPayment(@RequestParam String orderId,
                                        @RequestParam String paymentId,
                                        @RequestParam String razorpaySignature) {
        try {
            boolean isValid = paymentService.verifyPayment(orderId, paymentId, razorpaySignature);
            if (isValid) {
                return ResponseEntity.ok("Payment verified successfully");
            } else {
                return ResponseEntity.status(400).body("Payment verification failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error verifying payment");
        }
    }
}
