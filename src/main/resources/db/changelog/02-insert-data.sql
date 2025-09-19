--liquibase formatted sql

--changeset you:02-seed-accounts
INSERT INTO accounts (account_number, balance)
VALUES
    ('ACC1001', 1000.00),
    ('ACC1002', 2000.50),
    ('ACC1003', 500.75);

--changeset you:03-seed-transfers
INSERT INTO transfers (from_account_id, to_account_id, amount, status)
VALUES
    (1, 2, 150.00, 'COMPLETED'),
    (2, 3, 300.00, 'PENDING');
