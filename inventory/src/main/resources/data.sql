-- 1. Products
INSERT INTO products (name, description, category, price, supplier_id)
VALUES
    ('Laptop', 'High-performance laptop', 'Electronics', 1500.00, 1),
    ('Smartphone', 'Latest model smartphone', 'Electronics', 800.00, 2),
    ('Headphones', 'Noise-cancelling headphones', 'Electronics', 200.00, 3),
    ('Keyboard', 'Mechanical keyboard', 'Electronics', 120.00, 1),
    ('Mouse', 'Wireless mouse', 'Electronics', 50.00, 4);

-- 2. Warehouses
INSERT INTO warehouses (name, address)
VALUES
    ('Warehouse A', '123 Main St'),
    ('Warehouse B', '456 Industrial Rd'),
    ('Warehouse C', '789 Market Ave'),
    ('Warehouse D', '321 Distribution Blvd');

-- 3. Stock Levels (uses warehouse_id now)
INSERT INTO stock_levels (product_id, warehouse_id, quantity_available, quantity_sold, active)
VALUES
-- Laptop
(1, 1, 50, 20, TRUE),
(1, 2, 30, 10, TRUE),
(1, 3, 20, 5, TRUE),

-- Smartphone
(2, 1, 100, 40, TRUE),
(2, 3, 50, 15, TRUE),

-- Headphones
(3, 2, 150, 60, TRUE),
(3, 3, 100, 40, TRUE),

-- Keyboard
(4, 1, 200, 100, TRUE),
(4, 4, 150, 80, TRUE),

-- Mouse
(5, 2, 180, 90, TRUE),
(5, 4, 120, 50, TRUE);
