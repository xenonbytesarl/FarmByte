--Insert Product for testing purpose
INSERT INTO t_product (c_id, c_created_at, c_updated_at, c_image,c_name,c_purchasable,c_purchase_price,
   c_purchase_uom_id,c_reference,c_sale_price,c_sellable,c_stock_uom_id,c_type, c_category_id, c_active) values
 ('019180ab-9aea-73ce-9e4b-494c3b49282f', current_timestamp, null, 'products/product.png', 'Product.2', false, 0, null,
  null, 0, false, null, 'SERVICE', '01912c0f-2fcf-705b-ae59-d79d159f3ad0', true);