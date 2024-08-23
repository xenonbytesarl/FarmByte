create table if not exists t_product (
      c_id UUID not null ,
      c_created_at timestamp with time zone not null ,
      c_updated_at timestamp with time zone,
      c_reference varchar(16) ,
      c_name varchar(64) not null ,
      c_image varchar(255) not null ,
      c_type varchar(16) not null ,
      c_purchase_price numeric not null ,
      c_sale_price numeric not null ,
      c_sellable boolean not null ,
      c_purchasable boolean not null ,
      c_active boolean not null ,
      c_stock_uom_id UUID ,
      c_purchase_uom_id UUID ,
      c_category_id UUID not null ,
      constraint Pk_t_product_c_id primary key (c_id),
      constraint Uk_t_product_c_name unique (c_name),
      constraint Uk_t_product_c_reference unique (c_reference)
);

--- Foreign key constraint for t_product
ALTER TABLE t_product ADD CONSTRAINT Fk_t_product_c_category_id FOREIGN KEY (c_category_id) REFERENCES t_product_category (c_id);
ALTER TABLE t_product ADD CONSTRAINT Fk_t_product_c_stock_uom_id FOREIGN KEY (c_stock_uom_id) REFERENCES t_uom (c_id);
ALTER TABLE t_product ADD CONSTRAINT Fk_t_product_c_purchase_uom_id FOREIGN KEY (c_purchase_uom_id) REFERENCES t_uom (c_id);

--Create index for t_product
CREATE INDEX Ix_t_product_c_category_id ON t_product(c_category_id);
CREATE INDEX Ix_t_product_c_stock_uom_id ON t_product(c_stock_uom_id);
CREATE INDEX Ix_t_product_c_purchase_uom_id ON t_product(c_purchase_uom_id);
