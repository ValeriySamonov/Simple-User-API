-- Вставка тестовых пользователей
INSERT INTO users (username)
VALUES ('user1'), ('user2'), ('user3');

INSERT INTO contacts (contact_type, value, user_id)
VALUES (0, '+79001234567', 1),
       (1, 'user1@example.com', 1);

