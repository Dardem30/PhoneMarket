CREATE DATABASE phone_market;
USE phone_market;
CREATE TABLE products
(
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255)   NOT NULL,
    price      DECIMAL(10, 2) NOT NULL
);
CREATE TABLE coupons
(
    code           VARCHAR(255)              NOT NULL PRIMARY KEY,
    discount_type  ENUM ('fixed', 'percent') NOT NULL,
    discount_value DECIMAL(10, 2)            NOT NULL
);
CREATE TABLE countries
(
    country_id       INT AUTO_INCREMENT PRIMARY KEY,
    prefix           VARCHAR(2)     NOT NULL,
    name             VARCHAR(255)   NOT NULL,
    validation_regex VARCHAR(500)   NOT NULL,
    tax_rate         DECIMAL(10, 2) NOT NULL
);
CREATE INDEX ix_prefix ON countries(prefix);
