package com.demos.paymentsprocessingservice.payments.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private PaymentHistory(String userId, String paymentDescription, PaymentState paymentState,
                          LocalDateTime createdAt) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.paymentDescription = paymentDescription;
        this.paymentState = paymentState;
        this.createdAt = createdAt;
    }

    public static PaymentHistory newPaymentHistory(String userId, String description, LocalDateTime dateTime) {
        return new PaymentHistory(
                userId,
                description,
                PaymentState.NEW,
                dateTime
        );
    }

    public void balanceChecked() {
        this.paymentState = PaymentState.BALANCE_CHECKED;
    }

    public void readyToBeSent() {
        this.paymentState = PaymentState.READY_TO_BE_SENT;
    }

    public void complete() {
        this.paymentState = PaymentState.COMPLETED;
    }

    public void hasError(String error) {
        this.paymentState = PaymentState.ERROR;
        this.error = error;
    }

    public boolean isNew() {
        return this.paymentState.equals(PaymentState.NEW);
    }
}
