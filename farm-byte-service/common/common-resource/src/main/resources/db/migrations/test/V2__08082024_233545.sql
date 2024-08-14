-- uom_category
INSERT INTO t_uom_category (c_id, c_created_at, c_updated_at, c_name, c_active) VALUES ('01912c0f-2fcf-705b-ae59-d79d159f3ad0', current_timestamp, null, 'Unite', true);
INSERT INTO t_uom_category (c_id, c_created_at, c_updated_at, c_name, c_active) VALUES ('01912c2e-b52d-7b85-9c12-85af49fc7798', current_timestamp, null, 'Distance', true);

-- uom
INSERT INTO t_uom (c_id, c_created_at, c_updated_at, c_name, c_ratio, c_active, c_type, c_uom_category_id) VALUES  ('01912c2c-b81a-7245-bab1-aee9b97b2afb', current_timestamp, null, 'Unite', 1.0, true, 'REFERENCE', '01912c0f-2fcf-705b-ae59-d79d159f3ad0');
INSERT INTO t_uom (c_id, c_created_at, c_updated_at, c_name, c_ratio, c_active, c_type, c_uom_category_id) VALUES  ('01912c2e-b52d-7b85-9c12-85af49fc7798', current_timestamp, null, 'Carton de 5', 5.0, true, 'GREATER', '01912c0f-2fcf-705b-ae59-d79d159f3ad0');

