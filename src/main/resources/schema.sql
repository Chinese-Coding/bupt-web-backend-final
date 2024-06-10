-- 创建数据库
CREATE DATABASE book_online_shop;

-- 使用数据库
USE book_online_shop;
/*用户*/
CREATE TABLE users (
       user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
       username VARCHAR(50) NOT NULL UNIQUE,
       password VARCHAR(100) NOT NULL,
       level INT DEFAULT 1,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

/*商品管理*/
CREATE TABLE products (
                          product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          description TEXT,
                          category VARCHAR(50),
                          price DECIMAL(10, 2) NOT NULL,
                          stock INT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
                            category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(50) NOT NULL UNIQUE,
                            description TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_category (
                                  product_id BIGINT NOT NULL,
                                  category_id BIGINT NOT NULL,
                                  PRIMARY KEY (product_id, category_id),
                                  FOREIGN KEY (product_id) REFERENCES products(product_id),
                                  FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

/*订单*/
CREATE TABLE orders (
        order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
        user_id BIGINT NOT NULL,
        total_amount DECIMAL(10, 2) NOT NULL,
        status VARCHAR(20) DEFAULT 'PENDING',
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE order_items (
         order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
         order_id BIGINT NOT NULL,
         product_id BIGINT NOT NULL,
         quantity INT NOT NULL,
         price DECIMAL(10, 2) NOT NULL,
         FOREIGN KEY (order_id) REFERENCES orders(order_id),
         FOREIGN KEY (product_id) REFERENCES products(product_id)
);

/*评论*/
CREATE TABLE comments (
          comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
          user_id BIGINT NOT NULL,
          product_id BIGINT NOT NULL,
          content TEXT NOT NULL,
          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          FOREIGN KEY (user_id) REFERENCES users(user_id),
          FOREIGN KEY (product_id) REFERENCES products(product_id)
);


/*订单支付*/
Drop table if exists `payments`;
CREATE TABLE payments (
      payment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
      order_id BIGINT NOT NULL,
      payment_amount DECIMAL(10, 2) NOT NULL,
      payment_status VARCHAR(20) DEFAULT 'UNPAID',
      payment_date TIMESTAMP,
      FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
