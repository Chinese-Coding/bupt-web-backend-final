-- 由 GPT 生成的假数据, 不一定符合 实际场景 (没有考虑外键)

-- 插入假数据到分类表
INSERT INTO category (name, description)
VALUES
    ('Electronics', 'Devices and gadgets'),
    ('Books', 'Various kinds of books'),
    ('Clothing', 'Men and women clothing'),
    ('Home & Kitchen', 'Home appliances and kitchenware');

-- 插入假数据到用户表
INSERT INTO user (username, password, level)
VALUES
    ('john_doe', 'password123', 1),
    ('jane_smith', 'password456', 2),
    ('alice_jones', 'password789', 1),
    ('bob_brown', 'password101', 3);

-- 插入假数据到产品表
INSERT INTO product (name, description, category, price, stock)
VALUES
    ('Smartphone', 'A high-end smartphone', 'Electronics', 699.99, 50),
    ('Laptop', 'A powerful gaming laptop', 'Electronics', 1299.99, 30),
    ('Cookbook', 'Recipes for delicious meals', 'Books', 19.99, 100),
    ('T-shirt', 'Comfortable cotton t-shirt', 'Clothing', 9.99, 200),
    ('Blender', 'A high-speed blender', 'Home & Kitchen', 89.99, 40);

-- 插入假数据到商品分类关联表
INSERT INTO product_category (product_id, category_id)
VALUES
    (1, 1), -- Smartphone in Electronics
    (2, 1), -- Laptop in Electronics
    (3, 2), -- Cookbook in Books
    (4, 3), -- T-shirt in Clothing
    (5, 4); -- Blender in Home & Kitchen

-- 插入假数据到订单表
INSERT INTO orders (user_id, total_amount, status)
VALUES
    (1, 719.98, 'PENDING'),  -- john_doe's order
    (2, 1339.98, 'SHIPPED'), -- jane_smith's order
    (3, 19.99, 'DELIVERED'); -- alice_jones's order

-- 插入假数据到订单项表
INSERT INTO order_item (order_id, product_id, quantity, price)
VALUES
    (1, 1, 1, 699.99), -- john_doe's order, 1 smartphone
    (1, 3, 1, 19.99),  -- john_doe's order, 1 cookbook
    (2, 2, 1, 1299.99),-- jane_smith's order, 1 laptop
    (2, 4, 2, 19.98),  -- jane_smith's order, 2 t-shirts
    (3, 3, 1, 19.99);  -- alice_jones's order, 1 cookbook

-- 插入假数据到评论表
INSERT INTO comment (user_id, product_id, content)
VALUES
    (1, 1, 'Great smartphone, very satisfied!'),
    (2, 2, 'The laptop is amazing for gaming.'),
    (3, 3, 'Nice recipes, very helpful!');

-- 插入假数据到支付表
INSERT INTO payment (order_id, payment_amount, payment_status, payment_time)
VALUES
    (1, 719.98, 'PAID', CURRENT_TIMESTAMP), -- Payment for john_doe's order
    (2, 1339.98, 'PAID', CURRENT_TIMESTAMP), -- Payment for jane_smith's order
    (3, 19.99, 'PAID', CURRENT_TIMESTAMP); -- Payment for alice_jones's order
