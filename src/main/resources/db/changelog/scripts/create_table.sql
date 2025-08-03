--liquibase formatted sql
--changeset manh:1231

CREATE TABLE IF NOT EXISTS role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    age BIGINT,
    sex VARCHAR(255),
    phone_number VARCHAR(255),
    email VARCHAR(255),
    coins BIGINT,
    enabled BOOLEAN,
    verification_code VARCHAR(255),
    verification_code_expiration TIME,
    role_id BIGINT,
    CONSTRAINT FK_user_role FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE IF NOT EXISTS shop (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    address VARCHAR(255),
    type VARCHAR(255),
    rating DOUBLE,
    user_entity_id BIGINT,
    CONSTRAINT FK_shop_user FOREIGN KEY (user_entity_id) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS refresh_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    refresh_token LONGTEXT,
    user_id BIGINT,
    CONSTRAINT FK_refresh_token_user FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    price DOUBLE,
    category VARCHAR(255),
    quantity BIGINT,
    number_of_rate BIGINT,
    total_of_rate DOUBLE,
    rating DOUBLE,
    shop_id BIGINT,
    version BIGINT,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    CONSTRAINT FK_product_shop FOREIGN KEY (shop_id) REFERENCES shop(id)
);

CREATE TABLE IF NOT EXISTS image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(255),
    name VARCHAR(255),
    public_id VARCHAR(255),
    user_id BIGINT,
    product_id BIGINT,
    CONSTRAINT FK_image_user FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT FK_image_product FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE IF NOT EXISTS cart_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_date DATE,
    delivery_date DATE,
    quantity BIGINT,
    status VARCHAR(255),
    product_id BIGINT,
    user_id BIGINT,
    CONSTRAINT FK_cartItem_product FOREIGN KEY (product_id) REFERENCES product(id),
    CONSTRAINT FK_cartItem_user FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS rating (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(255),
    rating DOUBLE,
    user_id BIGINT,
    product_id BIGINT,
    CONSTRAINT FK_rating_user FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT FK_rating_product FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE IF NOT EXISTS user_shop (
    user_id BIGINT NOT NULL,
    shop_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, shop_id),
    CONSTRAINT FK_userShop_user FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT FK_userShop_shop FOREIGN KEY (shop_id) REFERENCES shop(id)
);
