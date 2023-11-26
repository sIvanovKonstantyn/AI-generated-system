package com.demos.paymentsprocessingservice.payments.exceptions;

public class InvalidPaymentAmountException extends RuntimeException {
    public InvalidPaymentAmountException(String message) {
        super(message);
    }
}