package com.demos.paymentsprocessingservice.payments.providers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "payment.system")
public class PaymentSystemUrlProvider {

    private Map<String, String> urls;

    @Value("${payment.system.default-url:http://localhost:8089/stub-payment-provider}")
    private String defaultURL;

    public String getPaymentSystemUrl(String serviceId) {
        return urls.getOrDefault(serviceId, defaultURL);
    }
}
