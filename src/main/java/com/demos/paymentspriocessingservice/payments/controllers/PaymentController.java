package com.demos.paymentspriocessingservice.payments.controllers;

import com.demos.paymentspriocessingservice.payments.models.PaymentDetails;
import com.demos.paymentspriocessingservice.payments.models.PaymentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @PostMapping
    public ResponseEntity<Void> createPayment(@RequestBody PaymentRequest paymentRequest) {
        // Mock logic for creating a payment
        // Add your actual logic here, e.g., call a service to process the payment

        // Assuming successful payment creation
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<PaymentDetails>> getAllPayments() {
        // Mock logic for getting all payments
        // Return a list of PaymentDetails or another suitable response based on your actual logic

        return ResponseEntity.ok(List.of(new PaymentDetails()));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDetails> getPaymentDetails(@PathVariable String paymentId) {
        // Mock logic for getting payment details
        // Return a PaymentDetails or another suitable response based on your actual logic

        return ResponseEntity.ok(new PaymentDetails());
    }
}