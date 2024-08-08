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


--Insert data for test
INSERT INTO t_uom_category (c_id, c_created_at, c_updated_at, c_name, c_active) VALUES ('01912c0f-2fcf-705b-ae59-d79d159f3ad0', current_timestamp, null, 'Unite', true);
INSERT INTO t_uom_category (c_id, c_created_at, c_updated_at, c_name, c_active) VALUES ('01912c2e-b52d-7b85-9c12-85af49fc7798', current_timestamp, null, 'Distance', true);
