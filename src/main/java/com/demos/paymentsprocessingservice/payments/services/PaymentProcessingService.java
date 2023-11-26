package com.demos.paymentsprocessingservice.payments.services;

import com.demos.paymentsprocessingservice.payments.exceptions.InsufficientBalanceException;
import com.demos.paymentsprocessingservice.payments.exceptions.InvalidPaymentAmountException;
import com.demos.paymentsprocessingservice.payments.exceptions.PaymentProcessingException;
import com.demos.paymentsprocessingservice.payments.providers.PaymentSystemUrlProvider;
import com.demos.paymentsprocessingservice.payments.clients.ServiceProviderClient;
import com.demos.paymentsprocessingservice.payments.clients.UserServiceClient;
import com.demos.paymentsprocessingservice.payments.models.PaymentDetails;
import com.demos.paymentsprocessingservice.payments.models.PaymentHistory;
import com.demos.paymentsprocessingservice.payments.models.PaymentRequest;
import com.demos.paymentsprocessingservice.payments.models.PaymentState;
import com.demos.paymentsprocessingservice.payments.repositories.PaymentHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for processing payment requests.
 * For detailed documentation, visit:
 * [Payments Processing Documentation](<a href="https://github.com/sIvanovKonstantyn/AI-generated-system/blob/main/documentation/payments%20processing.md">...</a>)
 */
@Service
@Slf4j
public class PaymentProcessingService {
    private final UserServiceClient userServiceClient;
    private final ServiceProviderClient serviceProviderClient;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final PaymentSystemUrlProvider paymentSystemUrlProvider;

    @Autowired
    public PaymentProcessingService(UserServiceClient userServiceClient,
                                    ServiceProviderClient serviceProviderClient,
                                    PaymentHistoryRepository paymentHistoryRepository,
                                    PaymentSystemUrlProvider paymentSystemUrlProvider) {
        this.userServiceClient = userServiceClient;
        this.serviceProviderClient = serviceProviderClient;
        this.paymentHistoryRepository = paymentHistoryRepository;
        this.paymentSystemUrlProvider = paymentSystemUrlProvider;
    }

    /**
     * Processes a payment request, including validation, balance check, system identification,
     * and sending the payment request to the service provider.
     *
     * @param paymentRequest The payment request to be processed.
     * @throws InvalidPaymentAmountException If the payment amount is invalid.
     * @throws InsufficientBalanceException   If the user has insufficient balance for the payment.
     * @throws PaymentProcessingException    If an error specific to payment processing occurs.
     * @throws RuntimeException              If an unexpected error occurs during payment processing.
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
            throw new PaymentProcessingException("Payment creation exception");
        }
    }

    private void validatePaymentRequest(PaymentRequest paymentRequest) {
        if (paymentRequest.getAmount() <= 0) {
            throw new InvalidPaymentAmountException("Invalid payment amount");
        }
    }

    private String identifyPaymentSystem(String serviceId) {
        return paymentSystemUrlProvider.getPaymentSystemUrl(serviceId);
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
            throw new InvalidPaymentAmountException("Invalid balance");
        }

        if (userBalance < requestedAmount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
    }

    private String savePaymentState(PaymentState paymentState, String userId, String paymentDescription, String error) {
        var paymentHistory = new PaymentHistory(userId, paymentDescription, paymentState, error);
        var savedHistory = paymentHistoryRepository.save(paymentHistory);
        return savedHistory.getId();
    }

    public List<PaymentDetails> getAllPayments() {
        return paymentHistoryRepository.getAllForUser(getUserFromSecurityContext())
                .stream()
                .map(PaymentDetails::new)
                .collect(Collectors.toList());
    }

    private String getUserFromSecurityContext() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public PaymentDetails getPaymentDetails(String paymentId) {
        return paymentHistoryRepository.getById(paymentId)
                .map(PaymentDetails::new)
                .orElseThrow(() -> new RuntimeException(""));
    }
}