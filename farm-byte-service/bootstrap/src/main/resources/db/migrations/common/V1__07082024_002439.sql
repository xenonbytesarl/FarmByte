create table if not exists t_uom (
    c_id UUID not null ,
    c_created_at timestamp with time zone not null ,
    c_updated_at timestamp with time zone,
    c_name varchar(64) not null ,
    c_ratio float8 not null ,
    c_active boolean not null ,
    c_type varchar(16) not null ,
    c_uom_category_id UUID not null ,
    constraint Pk_t_uom_c_id primary key (c_id),
    constraint Uk_t_uom_c_name unique (c_name)
);

create table if not exists t_uom_category (
     c_id UUID not null ,
     c_created_at timestamp with time zone not null ,
     c_updated_at timestamp with time zone,
     c_name varchar(64) not null ,
     c_active boolean not null ,
     constraint Pk_t_uom_category_c_id primary key (c_id),
     constraint Uk_t_uom_category_c_name unique (c_name)
);

--- Foreign key constraint for t_uom
ALTER TABLE t_uom ADD CONSTRAINT Fk_t_uom_c_uom_category_id FOREIGN KEY (c_uom_category_id) REFERENCES t_uom_category (c_id);

--Create index for t_uom
CREATE INDEX Ix_t_uom_c_uom_category_id ON t_uom(c_uom_category_id);