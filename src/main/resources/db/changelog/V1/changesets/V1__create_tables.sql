-- liquibase formatted sql

-- changeset yluttsev:create_product_category_table
CREATE TABLE IF NOT EXISTS product_category
(
    id   bigint PRIMARY KEY AUTO_INCREMENT,
    name varchar(255) NOT NULL UNIQUE
);

-- changeset yluttsev:create_client_category_table
CREATE TABLE IF NOT EXISTS client_category
(
    id   bigint PRIMARY KEY AUTO_INCREMENT,
    name varchar(255) NOT NULL UNIQUE
);

-- changeset yluttsev:create_discount_table
CREATE TABLE IF NOT EXISTS discount
(
    id                  bigint PRIMARY KEY AUTO_INCREMENT,
    product_category_id bigint REFERENCES product_category (id),
    client_category_id  bigint REFERENCES client_category (id),
    min_value           smallint     NOT NULL CHECK ( min_value BETWEEN 0 AND 100 ),
    max_value           smallint     NOT NULL CHECK ( max_value BETWEEN 0 AND 100 ),
    type                varchar(255) NOT NULL
);
