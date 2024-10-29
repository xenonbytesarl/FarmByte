-- Create table t_sequence
create table if not exists t_sequence (
    c_id UUID not null ,
    c_created_at timestamp with time zone not null ,
    c_updated_at timestamp with time zone,
    c_name varchar(128) not null ,
    c_code varchar(64) not null ,
    c_step integer not null ,
    c_size integer not null ,
    c_next integer not null ,
    c_prefix varchar(64) ,
    c_suffix varchar(64) ,
    c_active boolean not null ,
    constraint Pk_t_sequence_c_id primary key (c_id),
    constraint Uk_t_sequence_c_name unique (c_name),
    constraint Uk_t_sequence_c_code unique (c_code)
);
