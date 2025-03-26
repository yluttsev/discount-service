-- liquibase formatted sql

-- changeset yluttsev:add_discounts_for_client_with_id_1
INSERT INTO discount (id, product_category_id, client_category_id, min_value, max_value, type)
VALUES (1, 1, 1, 3, 5, 'VARIABLE'),
       (2, 2, 1, 5, 5, 'FIXED'),
       (3, 3, 1, 10, 10, 'FIXED'),
       (4, 4, 1, 5, 8, 'VARIABLE');

-- changeset yluttsev:add_discounts_fot_client_with_id_2
INSERT INTO discount (id, product_category_id, client_category_id, min_value, max_value, type)
VALUES (5, 1, 2, 10, 10, 'FIXED'),
       (6, 2, 2, 5, 10, 'VARIABLE'),
       (7, 3, 2, 10, 20, 'VARIABLE'),
       (8, 4, 2, 10, 10, 'FIXED');

-- changeset yluttsev:add_discounts_for_all_products
INSERT INTO discount (id, product_category_id, client_category_id, min_value, max_value, type)
VALUES (9, NULL, 1, 2, 5, 'VARIABLE'),
       (10, NULL, 2, 8, 8, 'FIXED');

-- changeset yluttsev:add_discounts_for_all_clients
INSERT INTO discount (id, product_category_id, client_category_id, min_value, max_value, type)
VALUES (11, 2, NULL, 5, 8, 'VARIABLE'),
       (12, 4, NULL, 6, 6, 'FIXED');
