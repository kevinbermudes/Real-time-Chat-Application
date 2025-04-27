-- Insertar usuario admin
INSERT INTO USERS (id, name, username, email, password, is_active, created_at, updated_at)
VALUES (1, 'Administrador', 'admin', 'admin@email.com',
        '$2a$10$DSGAfbOBz0Q5kNufMsmKoO37LQw4KUlt9K7lDO3jd3Kdt2pgT71Du', -- contraseña: admin123
        true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insertar roles del usuario (ADMIN y USER)
-- Inserta el rol ADMIN para ese usuario
INSERT INTO user_roles (user_id, roles)
VALUES (1, 'ADMIN');
-- resetea el auto incremental de la tabla USERS ya que se ha insertado un usuario con id 1 que es el administrador esto es para evitar conflictos
ALTER SEQUENCE users_seq RESTART WITH 2;

