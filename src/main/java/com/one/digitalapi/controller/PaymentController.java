package com.one.digitalapi.controller;

import com.one.digitalapi.entity.PaymentRequest;
import com.one.digitalapi.entity.PaymentResponse;
import com.one.digitalapi.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final Map<String, PaymentService> paymentServices;

    public PaymentController(Map<String, PaymentService> paymentServices) {
        this.paymentServices = paymentServices;
    }

    @PostMapping("/{gateway}")
    public PaymentResponse makePayment(@PathVariable String gateway, @RequestBody PaymentRequest request) {
        PaymentService paymentService = paymentServices.get(gateway + "PaymentService");
        if (paymentService == null) {
            return new PaymentResponse(null, "Failure", "Invalid Payment Gateway");
        }
        return paymentService.processPayment(request);
    }
}
