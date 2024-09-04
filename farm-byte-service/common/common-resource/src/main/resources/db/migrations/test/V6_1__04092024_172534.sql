--Insert Product for testing purpose
INSERT INTO t_product (c_id, c_created_at, c_updated_at, c_image,c_name,c_purchasable,c_purchase_price,
                       c_purchase_uom_id,c_reference,c_sale_price,c_sellable,c_stock_uom_id,c_type, c_category_id, c_active) values
    ('0191bda4-65e4-73a8-8291-b2870753ad00', current_timestamp, null, 'products/product.png', 'Mac Book Pro M2 Max', true, 36000,
     '01912c2e-b52d-7b85-9c12-85af49fc7798', '3254521',45000, true, '01912c2c-b81a-7245-bab1-aee9b97b2afb', 'STOCK', '01912c0f-2fcf-705b-ae59-d79d159f3ad0', true);

INSERT INTO t_product (c_id, c_created_at, c_updated_at, c_image,c_name,c_purchasable,c_purchase_price,
                       c_purchase_uom_id,c_reference,c_sale_price,c_sellable,c_stock_uom_id,c_type, c_category_id, c_active) values
    ('0191bda9-4b9f-7111-8bac-de8cc467715a', current_timestamp, null, 'products/product.png', 'HP Prolian Pro', true, 36000,
     '01912c2e-b52d-7b85-9c12-85af49fc7798', '63321457',45000, true, '01912c2c-b81a-7245-bab1-aee9b97b2afb', 'STOCK', '01912c0f-2fcf-705b-ae59-d79d159f3ad0', true);