package com.demos.paymentsprocessingservice.payments.providers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentSystemUrlProvider {

    @Value("#{${payment.system.urls}}")
    private Map<String, String> paymentSystemUrls;

    @Value("${payment.system.default-url:http://localhost:8089/stub-payment-provider}")
    private String defaultURL;
    public PaymentSystemUrlProvider(Map<String, String> paymentSystemUrls) {
        this.paymentSystemUrls = paymentSystemUrls;
    }

    public String getPaymentSystemUrl(String serviceId) {
        return paymentSystemUrls.getOrDefault(serviceId, defaultURL);
    }
}
