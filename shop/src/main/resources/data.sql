-- ---------------------------------------------
-- Seed Cart Items
-- ---------------------------------------------
INSERT INTO cart_items (user_id, product_id, quantity, price, supplier_id)
VALUES
    (1, 3, 2, 200.00, 3),   -- User 1 has 2 Headphones in cart
    (1, 5, 1, 50.00, 4),    -- User 1 has 1 Mouse in cart
    (2, 2, 1, 800.00, 1),   -- User 2 has 1 Smartphone in cart
    (2, 4, 2, 120.00, 1),   -- User 2 has 2 Keyboards in cart
    (3, 1, 1, 1500.00, 1),  -- User 3 has 1 Laptop in cart
    (3, 2, 1, 800.00, 1);   -- User 3 has 1 Smartphone in cart

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
    (1, 2, 1, 1, 800.00);   -- Smartphone

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
