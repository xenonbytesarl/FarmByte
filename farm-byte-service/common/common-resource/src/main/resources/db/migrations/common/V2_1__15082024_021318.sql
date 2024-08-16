alter table t_uom_category add column c_parent_id uuid;

--- Foreign key constraint for t_uom_category
alter table t_uom_category add constraint FK_t_uom_category_c_parent_id foreign key (c_parent_id) references t_uom_category (c_id);

--Create index for t_uom_category
create index Ix_t_uom_category_c_parent_id on t_uom_category(c_parent_id);