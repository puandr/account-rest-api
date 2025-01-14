-- Insert users
INSERT INTO users (id, username, password) VALUES
(1, 'johndoe', 'password1'), -- Replace with a hashed password in production
(2, 'janedoe', 'password2');

-- Insert accounts linked to users
INSERT INTO accounts (id, account_number, owner_name, created_at, user_id) VALUES
(1, '1234567890', 'John Doe', CURRENT_TIMESTAMP, 1),
(2, '0987654321', 'Jane Doe', CURRENT_TIMESTAMP, 2),
(3, '1111222233', 'John Doe', CURRENT_TIMESTAMP, 1); -- John has multiple accounts

INSERT INTO balances (id, account_id, currency, amount) VALUES
(1, 1, 'USD', 1000.00),
(2, 1, 'EUR', 500.00),
(3, 2, 'USD', 2000.00),
(4, 2, 'RUB', 1500.00);

--to avoid conflict with seeded data id-s.
ALTER TABLE balances ALTER COLUMN id RESTART WITH 5;
