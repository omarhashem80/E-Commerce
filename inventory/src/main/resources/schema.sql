CREATE TABLE products
(
    product_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description TEXT,
    category    VARCHAR(100),
    price       DECIMAL(10, 2) NOT NULL,
    active      BOOLEAN   DEFAULT TRUE,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    supplier_id BIGINT         NOT NULL
);

CREATE TABLE warehouses
(
    warehouse_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    address      VARCHAR(255) UNIQUE,
    active       BOOLEAN DEFAULT TRUE
);

CREATE TABLE stock_levels
(
    stock_id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id         BIGINT NOT NULL,
    warehouse_id       BIGINT NOT NULL,
    quantity_available INT    NOT NULL,
    quantity_sold      INT       DEFAULT 0,
    active             BOOLEAN   DEFAULT TRUE,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_stock_product FOREIGN KEY (product_id) REFERENCES products (product_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_stock_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses (warehouse_id)
        ON DELETE CASCADE,
    CONSTRAINT uq_product_warehouse UNIQUE (product_id, warehouse_id)
);
