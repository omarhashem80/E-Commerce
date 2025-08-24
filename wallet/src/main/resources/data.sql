-- Seed for users table
INSERT INTO users (email, username, first_name, last_name, role, password)
VALUES
    ('admin@example.com', 'admin1', 'Alice', 'Johnson', 'ADMIN', 'hashed_password1'),
    ('customer1@example.com', 'customer1', 'Bob', 'Smith', 'CUSTOMER', 'hashed_password2'),
    ('customer2@example.com', 'customer2', 'Charlie', 'Brown', 'CUSTOMER', 'hashed_password3'),
    ('supplier1@example.com', 'supplier1', 'David', 'Williams', 'SUPPLIER', 'hashed_password4');

-- Seed for wallets table (each user gets a wallet)
INSERT INTO wallets (user_id, balance)
VALUES
    (1, 1000.00),  -- Admin wallet
    (2, 250.50),   -- Customer 1
    (3, 500.00),   -- Customer 2
    (4, 0.00);     -- Supplier

-- Seed for wallet_transactions table (reference wallet_id, not user_id)
INSERT INTO wallet_transactions (wallet_id, amount, type)
VALUES
    (1, 1000.00, 'DEPOSIT'),    -- Admin initial deposit
    (2, 300.00, 'DEPOSIT'),     -- Customer 1 deposit
    (2, 49.50, 'WITHDRAWAL'),   -- Customer 1 withdrawal
    (3, 600.00, 'DEPOSIT'),     -- Customer 2 deposit
    (3, 100.00, 'WITHDRAWAL');  -- Customer 2 withdrawal
