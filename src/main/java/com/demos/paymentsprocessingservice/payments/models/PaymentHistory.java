package com.demos.paymentsprocessingservice.payments.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("payments")
public class PaymentHistory {
    @Id
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
