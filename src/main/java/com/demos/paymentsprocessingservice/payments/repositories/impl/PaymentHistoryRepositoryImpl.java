package com.demos.paymentsprocessingservice.payments.repositories.impl;

import com.demos.paymentsprocessingservice.payments.repositories.AggregateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentHistoryRepositoryImpl<T> implements AggregateRepository<T> {
    @Autowired
    private JdbcAggregateTemplate template;

    public T insert(T entity) {
        return template.insert(entity);
    }
}
