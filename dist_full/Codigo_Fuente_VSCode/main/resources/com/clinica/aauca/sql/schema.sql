-- Schema de la Base de Datos Clínica Aauca

CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    nombre_completo TEXT NOT NULL,
    rol TEXT CHECK(rol IN ('Admin', 'Médico', 'Recepción')) NOT NULL
);

-- Usuarios iniciales por defecto (Passwords hashed with BCrypt)
-- admin / admin123
INSERT OR IGNORE INTO usuarios (username, password, nombre_completo, rol)
VALUES ('admin', '$2a$12$o1yLTe.4SRKOaWLVVmsoueRzGsjiJ3Xp0TxJ3KV0K9zOhmUbqM2r6', 'Administrador Principal', 'Admin');

-- médico / medico123
INSERT OR IGNORE INTO usuarios (username, password, nombre_completo, rol)
VALUES ('medico', '$2a$12$nmd3s0HBqqzA9YR1ZW6vI.8bfSResIOTcgZa81XTgSaqG8KhjC5lS', 'Dr. Juan Pérez', 'Médico');

-- recepcion / recep123
INSERT OR IGNORE INTO usuarios (username, password, nombre_completo, rol)
VALUES ('recepcion', '$2a$12$lRnhHW1Mw4.tCL8wjoaVZ.k6b4pU9NvOyV.HN2AHUg4nEGpgsZyEW', 'Ana García', 'Recepción');
