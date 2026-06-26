-- Initial admin user
-- email: admin@ues.com
-- password: admin123 (BCrypt encoded)
-- role: ROLE_ADMIN

INSERT INTO users (dtype, email, password, name, created_at, phone_number, address, city, role)
SELECT 'ADMIN', 'admin@ues.com', '$2a$10$rS6Fg1HNqCbMKOxnq2FJyuHGU/FDhMaqSMGksCN7sFnQaXPfjKDmG', 'Admin', CURDATE(), NULL, 'Novi Sad', 'Novi Sad', 'ROLE_ADMIN'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@ues.com');
