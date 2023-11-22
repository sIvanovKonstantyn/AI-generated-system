package com.demos.paymentsprocessingservice.payments.models;

public enum PaymentState {
    NEW,
    BALANCE_CHECKED,
    READY_TO_BE_SENT,
    COMPLETED,
    ERROR
}