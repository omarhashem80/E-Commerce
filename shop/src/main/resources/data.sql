-- ---------------------------------------------
-- Seed Cart Items
-- ---------------------------------------------
INSERT INTO cart_items (user_id, product_id, quantity, price)
VALUES
    (1, 1, 2, 1500.00),  -- Laptop
    (1, 2, 1, 800.00),   -- Smartphone
    (2, 1, 1, 1500.00),  -- Laptop
    (2, 3, 4, 200.00),   -- Headphones
    (3, 4, 3, 120.00),   -- Keyboard
    (3, 5, 2, 50.00);    -- Mouse

-- ---------------------------------------------
-- Seed Orders
-- ---------------------------------------------
INSERT INTO orders (user_id, total_price, status, updated_at)
VALUES
    (1, 3800.00, 'PENDING', NOW()),   -- 2 Laptops + 1 Smartphone
    (2, 2300.00, 'PAID', NOW()),      -- 1 Laptop + 4 Headphones
    (3, 510.00, 'CANCELLED', NOW());  -- 3 Keyboards + 2 Mice

-- ---------------------------------------------
-- Seed Order Items
-- ---------------------------------------------
-- Order 1 (user 1)
INSERT INTO order_items (order_id, product_id, supplier_id, quantity, price)
VALUES
    (1, 1, 1, 2, 1500.00),  -- Laptop
    (1, 2, 2, 1, 800.00);   -- Smartphone

-- Order 2 (user 2)
INSERT INTO order_items (order_id, product_id, supplier_id, quantity, price)
VALUES
    (2, 1, 1, 1, 1500.00),  -- Laptop
    (2, 3, 3, 4, 200.00);   -- Headphones

-- Order 3 (user 3)
INSERT INTO order_items (order_id, product_id, supplier_id, quantity, price)
VALUES
    (3, 4, 1, 3, 120.00),   -- Keyboard
    (3, 5, 4, 2, 50.00);    -- Mouse
