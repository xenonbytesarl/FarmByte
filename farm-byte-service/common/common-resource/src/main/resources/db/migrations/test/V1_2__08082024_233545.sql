-- Insert t_uom_category
INSERT INTO t_uom_category (c_id, c_created_at, c_updated_at, c_name, c_active) VALUES ('01912c0f-2fcf-705b-ae59-d79d159f3ad0', current_timestamp, null, 'Unite', true);
INSERT INTO t_uom_category (c_id, c_created_at, c_updated_at, c_name, c_active) VALUES ('01912c2e-b52d-7b85-9c12-85af49fc7798', current_timestamp, null, 'Distance', true);
INSERT INTO t_uom_category (c_id, c_created_at, c_updated_at, c_name, c_active) VALUES ('01919905-c273-7aaa-8965-ef4ed404e4b9', current_timestamp, null, 'Temps', true);
INSERT INTO t_uom_category (c_id, c_created_at, c_updated_at, c_name, c_active) VALUES ('019285c7-7c30-7d3b-a0f6-d37f04b48d6a', current_timestamp, null, 'Fake Uom Category', true);

-- Insert t_uom
INSERT INTO t_uom (c_id, c_created_at, c_updated_at, c_name, c_ratio, c_active, c_type, c_uom_category_id) VALUES  ('01912c2c-b81a-7245-bab1-aee9b97b2afb', current_timestamp, null, 'Unite', 1.0, true, 'REFERENCE', '01912c0f-2fcf-705b-ae59-d79d159f3ad0');
INSERT INTO t_uom (c_id, c_created_at, c_updated_at, c_name, c_ratio, c_active, c_type, c_uom_category_id) VALUES  ('01912c2e-b52d-7b85-9c12-85af49fc7798', current_timestamp, null, 'Carton de 5', 5.0, true, 'GREATER', '01912c0f-2fcf-705b-ae59-d79d159f3ad0');
INSERT INTO t_uom (c_id, c_created_at, c_updated_at, c_name, c_ratio, c_active, c_type, c_uom_category_id) VALUES  ('0191d7ba-6781-7a63-9ec6-f0b99a908619', current_timestamp, null, 'Heure', 5.0, true, 'REFERENCE', '01919905-c273-7aaa-8965-ef4ed404e4b9');
INSERT INTO t_uom (c_id, c_created_at, c_updated_at, c_name, c_ratio, c_active, c_type, c_uom_category_id) VALUES  ('019199bf-2ea3-7ac1-8ad4-6dd062d5efec', current_timestamp, null, 'Jour', 24.0, true, 'GREATER', '01919905-c273-7aaa-8965-ef4ed404e4b9');
INSERT INTO t_uom (c_id, c_created_at, c_updated_at, c_name, c_ratio, c_active, c_type, c_uom_category_id) VALUES  ('019285be-b6bb-736e-b0a4-ab210ef5a59a', current_timestamp, null, 'metre(m)', 1.0, true, 'REFERENCE', '01912c2e-b52d-7b85-9c12-85af49fc7798');

-- Insert product_category
INSERT INTO t_product_category (c_id, c_created_at, c_updated_at, c_name, c_active) VALUES ('01912c0f-2fcf-705b-ae59-d79d159f3ad0', current_timestamp, null, 'Raw of material', true);
INSERT INTO t_product_category (c_id, c_created_at, c_updated_at, c_name, c_active) VALUES ('01912c2e-b52d-7b85-9c12-85af49fc7798', current_timestamp, null, 'Manufactured', true);
INSERT INTO t_product_category (c_id, c_created_at, c_updated_at, c_name, c_active) VALUES ('0191e077-b2a1-795e-8584-40e26a5fa850', current_timestamp, null, 'Fertilizer', true);

--Insert t_product
INSERT INTO t_product (c_id, c_created_at, c_updated_at, c_image,c_name,c_purchasable,c_purchase_price,
                       c_purchase_uom_id,c_reference,c_sale_price,c_sellable,c_stock_uom_id,c_type, c_category_id, c_active) values
    ('019180ab-9aea-73ce-9e4b-494c3b49282f', current_timestamp, null, 'products/product.png', 'Product.2', false, 0, null,
     null, 0, false, null, 'SERVICE', '01912c0f-2fcf-705b-ae59-d79d159f3ad0', true);

INSERT INTO t_product (c_id, c_created_at, c_updated_at, c_image,c_name,c_purchasable,c_purchase_price,
                       c_purchase_uom_id,c_reference,c_sale_price,c_sellable,c_stock_uom_id,c_type, c_category_id, c_active) values
    ('0191bda4-65e4-73a8-8291-b2870753ad00', current_timestamp, null, 'products/product.png', 'Mac Book Pro M2 Max', true, 36000,
     '01912c2e-b52d-7b85-9c12-85af49fc7798', '3254521',45000, true, '01912c2c-b81a-7245-bab1-aee9b97b2afb', 'STOCK', '01912c0f-2fcf-705b-ae59-d79d159f3ad0', true);

INSERT INTO t_product (c_id, c_created_at, c_updated_at, c_image,c_name,c_purchasable,c_purchase_price,
                       c_purchase_uom_id,c_reference,c_sale_price,c_sellable,c_stock_uom_id,c_type, c_category_id, c_active) values
    ('0191bda9-4b9f-7111-8bac-de8cc467715a', current_timestamp, null, '/Users/bamk/farmbyte/products/202409112207457910200_product_image.txt', 'HP Prolian Pro', true, 36000,
     '01912c2e-b52d-7b85-9c12-85af49fc7798', '63321457',45000, true, '01912c2c-b81a-7245-bab1-aee9b97b2afb', 'STOCK', '01912c0f-2fcf-705b-ae59-d79d159f3ad0', true);

