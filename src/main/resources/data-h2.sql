INSERT INTO address (id, street, city_id) VALUES (1, 'Strada', 1);
INSERT INTO donor (id, first_name, last_name, address_id, birth_date, weight, blood_group) VALUES (1, 'Ana', 'Pop', 1, '1990-05-01', 50, 'A_POSITIVE');
INSERT INTO transfusion_center (id, name, address_id) VALUES (1, 'Regina Maria', 1);
INSERT INTO appointment (id, donor_id, transfusion_center_id, time, status) VALUES (1, 1, 1, '2025-07-05T00:00:00.000Z', 'SCHEDULED');
INSERT INTO appointment (id, donor_id, transfusion_center_id, time, status) VALUES (2, 1, 1, '2025-05-05T00:00:00.000Z', 'COMPLETED');

INSERT INTO users(id, email, password, blockchain_address, role) VALUES(1, 'a@a.com', '$2a$10$iF2JFmVdGVj7v3DtA1qPbu2YdjnFuF5xh/nTs5Ht7FRUiKMh1wn7C', 'a', 'DONOR');