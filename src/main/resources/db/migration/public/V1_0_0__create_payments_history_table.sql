CREATE TABLE PAYMENTS (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    payment_description VARCHAR(255) NOT NULL,
    payment_state VARCHAR(50) NOT NULL,
    error VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_userId ON PAYMENTS(user_id);