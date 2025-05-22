-- user 테이블
CREATE TABLE IF NOT EXISTS user (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    email VARCHAR(255) NOT NULL UNIQUE,
    nickname VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    user_role ENUM('ADMIN', 'USER') NOT NULL
    );

-- book 테이블
CREATE TABLE IF NOT EXISTS book (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publisher VARCHAR(255),
    description TEXT,
    published_at DATE,
    price INT NOT NULL,
    stock INT NOT NULL,
    rating DOUBLE,
    user_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT FK_book_user FOREIGN KEY (user_id) REFERENCES user(id)
    );

-- orders 테이블
CREATE TABLE IF NOT EXISTS orders (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      status ENUM('CANCELED', 'COMPLETED', 'WAITING') NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT FK_orders_user FOREIGN KEY (user_id) REFERENCES user(id)
    );

-- order_item 테이블
CREATE TABLE IF NOT EXISTS order_item (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          book_id BIGINT NOT NULL,
                                          price BIGINT NOT NULL,
                                          quantity INT NOT NULL,
                                          order_id BIGINT,
                                          CONSTRAINT FK_order_item_book FOREIGN KEY (book_id) REFERENCES book(id),
    CONSTRAINT FK_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id)
    );
