create table if not exists t_product_category (
      c_id UUID not null ,
      c_created_at timestamp with time zone not null ,
      c_updated_at timestamp with time zone,
      c_name varchar(64) not null ,
      c_active boolean not null ,
      c_parent_id UUID,
      constraint Pk_t_product_category_c_id primary key (c_id),
      constraint Uk_t_product_category_c_name unique (c_name)
);

--- Foreign key constraint for t_product_category
alter table t_product_category add constraint FK_t_product_category_c_parent_id foreign key (c_parent_id) references t_product_category (c_id);

--Create index for t_product_category
create index Ix_t_product_category_c_parent_id on t_product_category(c_parent_id);



