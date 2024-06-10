-- 创建数据库
DROP DATABASE IF EXISTS shop;
CREATE DATABASE shop;

-- 使用数据库
USE shop;

-- 用户
DROP TABLE IF EXISTS user;
CREATE TABLE user
(
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    level       INT       DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 商品
DROP TABLE IF EXISTS product;
CREATE TABLE product
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)   NOT NULL,
    description TEXT,
    category    VARCHAR(50),
    price       DECIMAL(10, 2) NOT NULL,
    stock       INT            NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 分类
DROP TABLE IF EXISTS category;
CREATE TABLE category
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 商品分类关联表
DROP TABLE IF EXISTS product_category;
CREATE TABLE product_category
(
    product_id  BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, category_id)
);

/*订单*/
-- 订单
DROP TABLE IF EXISTS orders;
CREATE TABLE orders
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT         NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status       VARCHAR(20) DEFAULT 'PENDING',
    create_time  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

-- 订单项
DROP TABLE IF EXISTS order_item;
CREATE TABLE order_item
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT         NOT NULL,
    product_id BIGINT         NOT NULL,
    quantity   INT            NOT NULL,
    price      DECIMAL(10, 2) NOT NULL
);

-- 评论
DROP TABLE IF EXISTS comment;
CREATE TABLE comment
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    content     TEXT   NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- 订单支付
DROP TABLE IF EXISTS payment;
CREATE TABLE payment
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id       BIGINT         NOT NULL,
    payment_amount DECIMAL(10, 2) NOT NULL,
    payment_status VARCHAR(20) DEFAULT 'UNPAID',
    payment_time   TIMESTAMP
);
