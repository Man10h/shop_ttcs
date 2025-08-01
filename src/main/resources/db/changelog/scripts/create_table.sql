--liquibase formatted sql
--changelog manh:001

CREATE TABLE IF NOT EXISTS role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `user` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    age BIGINT,
    sex VARCHAR(255),
    phoneNumber VARCHAR(255),
    email VARCHAR(255),
    coins BIGINT,
    enabled BOOLEAN,
    verificationCode VARCHAR(255),
    verificationCodeExpiration TIME,
    roleId BIGINT,
    CONSTRAINT FK_user_role FOREIGN KEY (roleId) REFERENCES role(id)
);

CREATE TABLE IF NOT EXISTS shop (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    address VARCHAR(255),
    type VARCHAR(255),
    rating DOUBLE,
    userEntity_id BIGINT,
    CONSTRAINT FK_shop_user FOREIGN KEY (userEntity_id) REFERENCES user(id)
    );

CREATE TABLE IF NOT EXISTS refresh_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    refreshToken LONGTEXT,
    userId BIGINT,
    CONSTRAINT FK_refreshToken_user FOREIGN KEY (userId) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    price DOUBLE,
    category VARCHAR(255),
    quantity BIGINT,
    numberOfRate BIGINT,
    totalOfRate DOUBLE,
    rating DOUBLE,
    shopId BIGINT,
    version BIGINT,
    createdBy VARCHAR(255),
    lastModifiedBy VARCHAR(255),
    CONSTRAINT FK_product_shop FOREIGN KEY (shopId) REFERENCES shop(id)
);

CREATE TABLE IF NOT EXISTS image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(255),
    name VARCHAR(255),
    publicId VARCHAR(255),
    userId BIGINT,
    productId BIGINT,
    CONSTRAINT FK_image_user FOREIGN KEY (userId) REFERENCES user(id),
    CONSTRAINT FK_image_product FOREIGN KEY (productId) REFERENCES product(id)
);

CREATE TABLE IF NOT EXISTS cart_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    orderDate DATE,
    deliveryDate DATE,
    quantity BIGINT,
    status VARCHAR(255),
    productId BIGINT,
    userId BIGINT,
    CONSTRAINT FK_cartItem_product FOREIGN KEY (productId) REFERENCES product(id),
    CONSTRAINT FK_cartItem_user FOREIGN KEY (userId) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS rating (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(255),
    rating DOUBLE,
    userId BIGINT,
    productId BIGINT,
    CONSTRAINT FK_rating_user FOREIGN KEY (userId) REFERENCES user(id),
    CONSTRAINT FK_rating_product FOREIGN KEY (productId) REFERENCES product(id)
);

CREATE TABLE IF NOT EXISTS user_shop (
    userId BIGINT NOT NULL,
    shopId BIGINT NOT NULL,
    PRIMARY KEY (userId, shopId),
    CONSTRAINT FK_userShop_user FOREIGN KEY (userId) REFERENCES user(id),
    CONSTRAINT FK_userShop_shop FOREIGN KEY (shopId) REFERENCES shop(id)
);
