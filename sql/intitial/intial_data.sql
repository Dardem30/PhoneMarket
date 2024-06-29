INSERT INTO products (name, price)
VALUES ('Iphone', 100),
       ('Наушники', 20),
       ('Чехол', 10);
INSERT INTO coupons (code, discount_type, discount_value)
VALUES ('P10', 'percent', 10),
       ('P100', 'percent', 100);
INSERT INTO countries(prefix, name, validation_regex, tax_rate)
VALUES ('DE', 'Германия', '^DE\d{9}$', 19),
       ('IT', 'Италия', '^IT\d{9}$', 22),
       ('FR', 'Франция', '^FR[A-Z]{2}\d{9}$', 20),
       ('GR', 'Греция', '^GR\d{9}$', 24);