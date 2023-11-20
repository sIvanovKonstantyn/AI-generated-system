
## Flow

```mermaid
graph TD
  A1((Start)) --> A
  A[Receive Payment Request] --> B[Save Payment State - New]
  B --> B1[Check User Balance] --> C[Save Payment State - Balance Checked]
  B1 --> |Has negative balance after potential transaction| F[Save Payment State - Error]
  C --> C1[Identify Payment System based on service provider and config] --> D[Save Payment State - Ready to Be Sent]
  D --> D1[Send Payment Request to Provider]
  D1 -->|Expected Error| E[Retry - Number of Times]
  E --> D1
  D1 -->|Unexpected Error| F
  F -->|Notify About Error| G((End))
  D1 -->|Successful| H[Save Payment State - Completed]
  H -->|End| G

  style A fill:#86C7F3,stroke:#000
  style B fill:#86C7F3,stroke:#000
  style B1 fill:#86C7F3,stroke:#000
  style C fill:#86C7F3,stroke:#000
  style C1 fill:#86C7F3,stroke:#000
  style D fill:#86C7F3,stroke:#000
  style D1 fill:#86C7F3,stroke:#000 
  style E fill:#FF7F7F,stroke:#000
  style F fill:#FF7F7F,stroke:#000
  style H fill:#86C7F3,stroke:#000
```

	1. Receive Payment Request: Upon receiving and validating a payment request, the system proceeds to save the payment state as “New.” 
	2. Save Payment State - New: The system saves the payment state as “New” and then checks the user’s balance. 
	3. Check User Balance: The system verifies if the user has a sufficient balance for the payment. 
	4. Save Payment State - Balance Checked: After checking the user’s balance: 
	  • If the balance is positive, the payment state is saved as “Balance Checked.” 
	  • If the balance is not positive, the system saves it as an error and goes to the end of the flow. 
	5. Identify Payment System based on service provider and config: Using the service provider and configuration information, the system identifies the appropriate payment system and saves the payment state as “Ready to Be Sent.” 
	6. Save Payment State - Ready to Be Sent: The system is now prepared to send the validated payment request to the identified provider. 
	7. Send Payment Request to Provider: The system attempts to send the payment request to the provider, and the subsequent steps depend on the outcome: 
	  • Expected Error: If an expected error occurs during the sending process, the system retries a certain number of times. 
	  • Retry - Number of Times: The system retries sending the payment request to the provider. 
	  • Unexpected Error: In case of an unexpected error during the sending process or if the number of retries was succeeded, the system saves the payment state as “Error.” 
	  • Save Payment State - Error: The system saves the payment state as “Error” and notifies about the error. 
	  • Notify About Error: The system notifies relevant parties about the error. 
	  • Successful: If the payment request is successfully processed by the provider, the system saves the payment state as “Completed.” 
	  • Save Payment State - Completed: The system saves the payment state as “Completed” after a successful transaction. 
