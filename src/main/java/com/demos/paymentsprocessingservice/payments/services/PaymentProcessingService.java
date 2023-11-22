package com.demos.paymentsprocessingservice.payments.services;

import com.demos.paymentsprocessingservice.payments.models.PaymentHistory;
import com.demos.paymentsprocessingservice.payments.repositories.PaymentHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demos.paymentsprocessingservice.payments.models.PaymentState;
import com.demos.paymentsprocessingservice.payments.models.PaymentRequest;
import com.demos.paymentsprocessingservice.payments.clients.UserServiceClient;
import com.demos.paymentsprocessingservice.payments.clients.ServiceProviderClient;

/**
 * Service responsible for processing payment requests.
 * For detailed documentation, visit: [Payments Processing Documentation](https://github.com/sIvanovKonstantyn/AI-generated-system/blob/main/documentation/payments%20processing.md)
 */
@Service
@Slf4j
public class PaymentProcessingService {
    private final UserServiceClient userServiceClient;
    private final ServiceProviderClient serviceProviderClient;
    private final PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    public PaymentProcessingService(UserServiceClient userServiceClient,
                                    ServiceProviderClient serviceProviderClient,
                                    PaymentHistoryRepository paymentHistoryRepository) {
        this.userServiceClient = userServiceClient;
        this.serviceProviderClient = serviceProviderClient;
        this.paymentHistoryRepository = paymentHistoryRepository;
    }

    /**
     * Processes a payment request, including validation, balance check, system identification,
     * and sending the payment request to the service provider.
     *
     * @param paymentRequest The payment request to be processed.
     * @throws Exception If an error occurs during payment processing.
     */
    public String createPayment(PaymentRequest paymentRequest) {
        try {
            log.atInfo().log(
                "Payment with description '{}' started processing for the user {}",
                paymentRequest.getDescription(),
                paymentRequest.getUserId()
            );

            validatePaymentRequest(paymentRequest);

            String paymentId = savePaymentState(PaymentState.NEW, paymentRequest.getUserId(),
                paymentRequest.getDescription(), null);

            checkUserBalance(paymentRequest.getUserId(), paymentRequest.getAmount());

            savePaymentState(PaymentState.BALANCE_CHECKED, paymentRequest.getUserId(),
                paymentRequest.getDescription(), null);

            String paymentSystem = identifyPaymentSystem(paymentRequest.getServiceId());

            savePaymentState(PaymentState.READY_TO_BE_SENT, paymentRequest.getUserId(),
                paymentRequest.getDescription(), null);

            sendPaymentRequest(paymentSystem, paymentRequest);

            log.atInfo().log(
                "Payment with description '{}' finished processing for the user {}",
                paymentRequest.getDescription(),
                paymentRequest.getUserId()
            );

            return paymentId;

        } catch (Exception e) {
            log.atError().setCause(e).log(e.getMessage());
            savePaymentState(PaymentState.ERROR, paymentRequest.getUserId(),
                paymentRequest.getDescription(), e.getMessage());

            notifyAboutError();
            throw new Exception("Payment creation exception");
        }
    }

    private void validatePaymentRequest(PaymentRequest paymentRequest) throws Exception {
        if (paymentRequest.getAmount() <= 0) {
            throw new Exception("Invalid payment amount");
        }
    }

    private String identifyPaymentSystem(String serviceId) {
        return "MockPaymentSystem"; // Mock logic to identify payment system
    }

    private void notifyAboutError() {
        System.out.println("Notifying about error...");
    }
    
    private void sendPaymentRequest(String paymentSystem, PaymentRequest paymentRequest) {
        serviceProviderClient.sendPayment(paymentSystem, paymentRequest);
    }

    private void checkUserBalance(String userId, Integer requestedAmount) throws Exception {
        int userBalance = userServiceClient.getBalance(userId);

        if (userBalance <= 0) {
            throw new Exception("Invalid balance");
        }

        if (userBalance < requestedAmount) {
            throw new Exception("Insufficient balance");
        }
    }

    private String savePaymentState(PaymentState paymentState, String userId, String paymentDescription, String error) {
        PaymentHistory paymentHistory = new PaymentHistory(userId, paymentDescription, paymentState, error);
        PaymentHistory savedHistory = paymentHistoryRepository.save(paymentHistory);
        return savedHistory.getId();
    }

        public List<PaymentDetails> getAllPayments() {
        // Mock logic for getting all payments
        // Return a list of PaymentDetails or another suitable response based on your actual logic
        return paymentHistoryRepository.getAllForUser(getUserFromSecurityContext())
                .map(ph -> new PaymentDetails(ph))
                .collect(Collectors.toList());
    }

    public PaymentDetails getPaymentDetails(String paymentId) {
        return paymentHistoryRepository.getById(paymentId)
                .map(ph -> new PaymentDetails(ph))
                .collect(Collectors.toList());
    }
}