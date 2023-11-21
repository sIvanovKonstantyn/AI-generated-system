import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demos.paymentsprocessingservice.payments.models.PaymentState;
import com.demos.paymentsprocessingservice.payments.models.PaymentRequest;
import com.demos.paymentsprocessingservice.payments.clients.UserServiceClient;
import com.demos.paymentsprocessingservice.payments.clients.ServiceProviderClient;

@Service
public class PaymentProcessingService {

    private final UserServiceClient userServiceClient;
    private final ServiceProviderClient serviceProviderClient;
    private final PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    public PaymentProcessingService(UserServiceClient userServiceClient, ServiceProviderClient serviceProviderClient, PaymentHistoryRepository paymentHistoryRepository) {
        this.userServiceClient = userServiceClient;
        this.serviceProviderClient = serviceProviderClient;
        this.paymentHistoryRepository = paymentHistoryRepository;
    }

    public void createPayment(PaymentRequest paymentRequest) {
        try {
            validatePaymentRequest(paymentRequest);

            savePaymentState(PaymentState.NEW, paymentRequest.getUserId(), paymentRequest.getDescription(), null);

            checkUserBalance(paymentRequest.getUserId(), paymentRequest.getAmount());

            savePaymentState(PaymentState.BALANCE_CHECKED, paymentRequest.getUserId(), paymentRequest.getDescription(), null);

            String paymentSystem = identifyPaymentSystem(paymentRequest.getServiceId());

            savePaymentState(PaymentState.READY_TO_BE_SENT, paymentRequest.getUserId(), paymentRequest.getDescription(), null);

            sendPaymentRequest(paymentSystem, paymentRequest);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            savePaymentState(PaymentState.ERROR);
            notifyAboutError();
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

        if (mockUserBalance < requestedAmount) {
            throw new Exception("Insufficient balance");
        }
    }

    private void savePaymentState(PaymentState paymentState, String userId, String paymentDescription, String error) {
        PaymentHistory paymentHistory = new PaymentHistory(userId, paymentDescription, paymentState, error);
        paymentHistoryRepository.save(paymentHistory);
    }
}