-- Cart items table
-- Create database shopdb;
-- use shopdb;
CREATE TABLE cart_items
(
    cart_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT         NOT NULL,
    product_id   BIGINT         NOT NULL,
    supplier_id  BIGINT         NOT NULL,
    quantity     INT            NOT NULL,
    price        DECIMAL(15, 2) NOT NULL
);

-- Orders table
CREATE TABLE orders
(
    order_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT         NOT NULL,
    total_price DECIMAL(15, 2) NOT NULL,
    status      ENUM('PENDING','PAID','CANCELLED') DEFAULT 'PENDING',
    active      BOOLEAN   DEFAULT TRUE,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Order items table
CREATE TABLE order_items
(
    order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id      BIGINT         NOT NULL,
    product_id    BIGINT         NOT NULL,
    supplier_id   BIGINT         NOT NULL,
    quantity      INT            NOT NULL,
    price         DECIMAL(15, 2) NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders (order_id)
        ON DELETE CASCADE
);
