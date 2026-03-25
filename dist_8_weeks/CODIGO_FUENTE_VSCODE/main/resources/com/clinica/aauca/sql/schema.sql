-- Schema de la Base de Datos Clínica Aauca

CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    nombre_completo TEXT NOT NULL,
    rol TEXT CHECK(rol IN ('Admin', 'Médico', 'Recepción')) NOT NULL
);

-- Tabla de Pacientes
CREATE TABLE IF NOT EXISTS pacientes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    dni TEXT UNIQUE NOT NULL,
    nombre_completo TEXT NOT NULL,
    telefono TEXT,
    email TEXT,
    direccion TEXT,
    fecha_nacimiento TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Citas Médicas
CREATE TABLE IF NOT EXISTS citas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    paciente_id INTEGER NOT NULL,
    medico_id INTEGER NOT NULL,
    fecha_cita TEXT NOT NULL,
    hora_cita TEXT NOT NULL,
    estado TEXT DEFAULT 'PENDIENTE', -- PENDIENTE, COMPLETADA, CANCELADA
    motivo TEXT,
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    FOREIGN KEY (medico_id) REFERENCES usuarios(id)
);

-- Tabla de Historias Clínicas
CREATE TABLE IF NOT EXISTS historias_clinicas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    paciente_id INTEGER NOT NULL,
    medico_id INTEGER NOT NULL,
    fecha_consulta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    motivo_consulta TEXT,
    diagnostico TEXT,
    tratamiento TEXT,
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    FOREIGN KEY (medico_id) REFERENCES usuarios(id)
);

-- Tabla de Inventario (Farmacia)
CREATE TABLE IF NOT EXISTS productos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    descripcion TEXT,
    precio REAL NOT NULL,
    stock INTEGER DEFAULT 0,
    categoria TEXT
);

-- Tabla de Facturación
CREATE TABLE IF NOT EXISTS facturas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    paciente_id INTEGER NOT NULL,
    fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    subtotal REAL,
    impuestos REAL,
    total REAL,
    estado TEXT DEFAULT 'PAGADA',
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id)
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
