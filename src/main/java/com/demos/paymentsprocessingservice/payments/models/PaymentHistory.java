package com.demos.paymentsprocessingservice.payments.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentHistory {
    private String userId;
    private String paymentDescription;
    private PaymentState paymentState;
    private String error;
}
