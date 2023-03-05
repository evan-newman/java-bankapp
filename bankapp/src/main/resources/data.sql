INSERT INTO USERS (first_name, last_name, email, password)
VALUES
    ('John', 'Doe', 'jdoe@example.com', '{bcrypt}$2a$04$5iuUb1JiORMC34QH4eIqX.ACE2fbeXNtOLnwOwcdavBNvRyofTKX.'),
    ('Jane', 'admin', 'jadmin@example.com', '{bcrypt}$2a$04$ESayQAnAsbC/v47eJJ7Nx.CNgFTsjS9LvJuOeaqw5LfYjZJd7ZRey');

-- John Doe pW: test123
-- Jane admin pw: test456

INSERT INTO ROLES (name)
VALUES
    ('ROLE_USER'),
    ('ROLE_ADMIN');

INSERT INTO USER_ROLES (user_id, role_id)
VALUES
    (1, 1),
    (2, 2);

INSERT INTO ACCOUNT_TYPES (type_name)
VALUES
    ('checking'),
    ('savings'),
    ('loan'),
    ('credit card');

INSERT INTO ACCOUNTS (user_id, name, account_type_id, open)
VALUES
    (1, 'checking', 1, 1),
    (1, 'savings', 2, 1),
    (1, '1965 aston martin db car loan', 3, 1),
    (1, 'Mastercard', 4, 1),
    (2, 'checkings', 1, 1);

INSERT INTO ACCOUNT_ENTRIES (account_id, amount, entry_date)
VALUES
    (1, 1500, '2023-02-28 12:37:00'),
    (2, 2500, '2023-02-28 12:37:00'),
    (3, -2000, '2023-02-28 12:37:00'),
    (4, -500, '2023-02-28 12:37:00'),
    (5, 1000, '2023-02-28 12:37:00');

INSERT INTO LOAN_DETAILS (account_id, creation_date, original_amount, monthly_payment, interest_rate, monthly_term)
VALUES
    (3, '2022-10-15 18:09:00', -10000, 208.34, 0.05, 60);

INSERT INTO CREDIT_DETAILS (account_id, credit_limit, creation_date, cc_number, month_expire, year_expire, ccv)
VALUES
    (4, -2000, '2020-03-19 13:42:00', '1234567812345678', 5, 2026, 776);

INSERT INTO USER_ROLES (user_id, role_id) 
VALUES 
    (1, 1), 
    (2, 2);