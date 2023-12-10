package com.demos.paymentsprocessingservice.payments.repositories;

public interface AggregateRepository <T> {
    T insert(T entity);
}
