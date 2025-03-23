-- liquibase formatted sql

-- changeset yluttsev:insert_client_categories
INSERT INTO client_category (id, name)
VALUES (1, 'Покупатель'),
       (2, 'Постоянный клиент');


-- changeset yluttsev:insert_product_categories
INSERT INTO product_category (id, name)
VALUES (1, 'Электроника'),
       (2, 'Одежда'),
       (3, 'Еда'),
       (4, 'Бытовая химия');
