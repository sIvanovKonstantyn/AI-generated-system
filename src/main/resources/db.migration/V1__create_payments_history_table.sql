CREATE TABLE Payment (
    id VARCHAR(255) PRIMARY KEY,
    userId VARCHAR(255) NOT NULL,
    paymentDescription VARCHAR(255) NOT NULL,
    paymentState VARCHAR(50) NOT NULL,
    error VARCHAR(255),
    CONSTRAINT fk_user FOREIGN KEY (userId) REFERENCES User(id) ON DELETE CASCADE,
    INDEX idx_userId (userId)
);