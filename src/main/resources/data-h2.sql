INSERT INTO donor (id, first_name, last_name, birth_date, weight, blood_group) VALUES (1, 'Ana', 'Pop', '1990-05-01', 50, 'A_POSITIVE');
INSERT INTO address (id, street, city_id) VALUES (1, 'Strada', 1);
INSERT INTO transfusion_center (id, name, address_id) VALUES (1, 'Regina Maria', 1);
INSERT INTO appointment (id, donor_id, transfusion_center_id, time, status) VALUES (1, 1, 1, '2025-05-05T00:00:00.000Z', 'COMPLETED');