-- Create table t_inventory_emplacement
create table if not exists t_inventory_emplacement (
    c_id UUID not null ,
    c_created_at timestamp with time zone not null ,
    c_updated_at timestamp with time zone,
    c_name varchar(64) not null ,
    c_type varchar(64) not null ,
    c_parent_id UUID ,
    c_active boolean not null ,
    constraint Pk_t_inventory_emplacement_c_id primary key (c_id),
    constraint Uk_t_inventory_emplacement_c_name unique (c_name)
);

--- Foreign key constraint for t_inventory_emplacement
ALTER TABLE t_inventory_emplacement ADD CONSTRAINT Fk_t_inventory_emplacement_c_parent_id FOREIGN KEY (c_parent_id) REFERENCES t_inventory_emplacement (c_id);
