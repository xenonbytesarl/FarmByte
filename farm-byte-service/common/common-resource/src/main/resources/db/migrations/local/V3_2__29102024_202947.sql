-- Insert t_sequence
INSERT INTO t_sequence (c_id, c_created_at, c_updated_at, c_name, c_code, c_step, c_next, c_size, c_prefix, c_active)
VALUES ('0192d9c7-9b58-751d-9c66-5308ceb53bd4', current_timestamp, null, 'Receipt Sequence', 'TRANSFER_RECEIPT', 1, 1, 5, 'IN\%(year)s%(month)s\', true);
VALUES ('0192d9c7-cf1a-73d7-8326-5816a9f039d3', current_timestamp, null, 'Delivery Sequence', 'TRANSFER_DELIVERY', 1, 1, 5, 'OUT\%(year)s%(month)s\', true);
VALUES ('0192d9c7-f126-77e7-862a-e8478ee8cb01', current_timestamp, null, 'Internal Sequence', 'TRANSFER_INTERNAL', 1, 1, 5, 'INT\%(year)s%(month)s\', true);