-- SET REFERENTIAL_INTEGRITY FALSE;

-- DROP TABLE IF EXISTS wallet_transactions;
-- DROP TABLusersE IF EXISTS wallets;
-- DROP TABLE IF EXISTS users;

-- SET REFERENTIAL_INTEGRITY TRUE;

-- CREATE DATABASE IF NOT EXISTS walletdb;
-- CREATE DATABASE walletdb;
-- USE walletdb;

-- 1. users table
CREATE TABLE users
(
    user_id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    email               VARCHAR(255) UNIQUE NOT NULL,
    user_name            VARCHAR(255) UNIQUE NOT NULL,
    first_name          VARCHAR(100)        NOT NULL,
    last_name           VARCHAR(100)        NOT NULL,
    role                ENUM('CUSTOMER', 'ADMIN', 'SUPPLIER') NOT NULL,
    password            VARCHAR(255)        NOT NULL,
    password_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active              BOOLEAN   DEFAULT TRUE,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. wallets table
CREATE TABLE wallets
(
    wallet_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    balance    DECIMAL(15, 2) DEFAULT 0.00,
    created_at TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    active     BOOLEAN        DEFAULT TRUE,
    CONSTRAINT fk_wallets_user FOREIGN KEY (user_id) REFERENCES users (user_id)
        ON DELETE CASCADE
);

-- 3. wallet_transactions table
CREATE TABLE wallet_transactions
(
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    wallet_id      BIGINT         NOT NULL,
    amount         DECIMAL(15, 2) NOT NULL,
    type           ENUM('DEPOSIT', 'WITHDRAWAL') NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active         BOOLEAN   DEFAULT TRUE,
    CONSTRAINT fk_transactions_wallet FOREIGN KEY (wallet_id) REFERENCES wallets (wallet_id)
        ON DELETE CASCADE
);
