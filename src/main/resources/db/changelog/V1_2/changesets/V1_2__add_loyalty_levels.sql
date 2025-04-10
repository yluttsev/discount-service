-- liquibase formatted sql

-- changeset yluttsev:create_loyalty_level_table
CREATE TABLE IF NOT EXISTS loyalty_level
(
    id         bigint PRIMARY KEY AUTO_INCREMENT,
    min_amount numeric(11, 2) NOT NULL,
    max_amount numeric(11, 2) NOT NULL,
    percent    smallint       NOT NULL CHECK ( percent BETWEEN 0 AND 100 )
);

-- changeset yluttsev:add_loyalty_levels
INSERT INTO loyalty_level (id, min_amount, max_amount, percent)
VALUES (1, 0, 5000, 0),
       (2, 5001, 10000, 3),
       (3, 10001, 20000, 7),
       (4, 20001, 50000, 10),
       (5, 50001, 10000000, 13);