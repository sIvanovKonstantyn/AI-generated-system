package com.demos.paymentsprocessingservice.payments.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentHistory {
    private String id;
    private String userId;
    private String paymentDescription;
    private PaymentState paymentState;
    private String error;

    public PaymentHistory(String userId, String paymentDescription, PaymentState paymentState, String error) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.paymentDescription = paymentDescription;
        this.paymentState = paymentState;
        this.error = error;
    }
}
