-- Schema de la Base de Datos Clínica Aauca v3.9 PLATINUM
-- Configurado con datos de Guinea Ecuatorial

CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    nombre_completo TEXT NOT NULL,
    rol TEXT CHECK(rol IN ('Admin', 'Médico', 'Enfermero', 'Recepción')) NOT NULL
);

-- Tabla de Pacientes
CREATE TABLE IF NOT EXISTS pacientes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    dni TEXT UNIQUE NOT NULL, -- DIP o Pasaporte
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

-- NUEVA TABLA: Hospitalización
CREATE TABLE IF NOT EXISTS hospitalizaciones (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    paciente_id INTEGER NOT NULL,
    fecha_ingreso TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_alta TIMESTAMP,
    numero_cama TEXT,
    motivo TEXT,
    estado TEXT CHECK(estado IN ('ACTIVO', 'ALTA')) DEFAULT 'ACTIVO',
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id)
);

-- NUEVA TABLA: Evolución de Paciente (Gráfica de Constantes)
CREATE TABLE IF NOT EXISTS evoluciones (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    hospitalizacion_id INTEGER NOT NULL,
    enfermero_id INTEGER NOT NULL,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    temperatura REAL,
    presion_arterial TEXT,
    frecuencia_cardiaca INTEGER,
    observaciones TEXT,
    FOREIGN KEY (hospitalizacion_id) REFERENCES hospitalizaciones(id),
    FOREIGN KEY (enfermero_id) REFERENCES usuarios(id)
);

-- Tabla de Inventario (Farmacia)
CREATE TABLE IF NOT EXISTS productos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    descripcion TEXT,
    precio REAL NOT NULL,
    stock INTEGER DEFAULT 0,
    alerta_minima INTEGER DEFAULT 5,
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

-- DATOS INICIALES: GUINEA ECUATORIAL (Malabo / Bata)
-- Passwords hashed with BCrypt

-- Usuarios
INSERT OR IGNORE INTO usuarios (username, password, nombre_completo, rol) VALUES ('admin', '$2a$12$o1yLTe.4SRKOaWLVVmsoueRzGsjiJ3Xp0TxJ3KV0K9zOhmUbqM2r6', 'Admin Clínica Malabo', 'Admin');
INSERT OR IGNORE INTO usuarios (username, password, nombre_completo, rol) VALUES ('medico1', '$2a$12$nmd3s0HBqqzA9YR1ZW6vI.8bfSResIOTcgZa81XTgSaqG8KhjC5lS', 'Dr. Manuel Ondo Mba', 'Médico');
INSERT OR IGNORE INTO usuarios (username, password, nombre_completo, rol) VALUES ('enfermero1', '$2a$12$lRnhHW1Mw4.tCL8wjoaVZ.k6b4pU9NvOyV.HN2AHUg4nEGpgsZyEW', 'Enf. Justina Nkogo', 'Enfermero');

-- Pacientes Locales
INSERT OR IGNORE INTO pacientes (dni, nombre_completo, telefono, direccion, fecha_nacimiento) VALUES ('DIP-001234', 'Santiago Obiang Mangue', '+240 222 000 111', 'Malabo II, Bioko Norte', '1985-05-12');
INSERT OR IGNORE INTO pacientes (dni, nombre_completo, telefono, direccion, fecha_nacimiento) VALUES ('DIP-005678', 'Consuelo Ayecaba Nchama', '+240 222 555 666', 'Barrio Buena Esperanza, Bata', '1992-11-20');
INSERT OR IGNORE INTO pacientes (dni, nombre_completo, telefono, direccion, fecha_nacimiento) VALUES ('DIP-009988', 'José María Nguema', '+240 555 123 456', 'Centro Ciudad, Ebibeyin', '1970-02-15');

-- Farmacia y Servicios Médicos
INSERT OR IGNORE INTO productos (nombre, descripcion, precio, stock, alerta_minima, categoria) VALUES ('Consulta General', 'Evaluación médica inicial', 5000.0, 999, 0, 'Servicio');
INSERT OR IGNORE INTO productos (nombre, descripcion, precio, stock, alerta_minima, categoria) VALUES ('Ecografía Abdominal', 'Examen por imagen', 15000.0, 50, 0, 'Examen');
INSERT OR IGNORE INTO productos (nombre, descripcion, precio, stock, alerta_minima, categoria) VALUES ('Perfil Renal (Lab)', 'Análisis de sangre', 8000.0, 100, 0, 'Laboratorio');
INSERT OR IGNORE INTO productos (nombre, descripcion, precio, stock, alerta_minima, categoria) VALUES ('Artesunato (Iny)', 'Tratamiento Malaria Severa', 3500.0, 50, 10, 'Antimalárico');
INSERT OR IGNORE INTO productos (nombre, descripcion, precio, stock, alerta_minima, categoria) VALUES ('Paracetamol 500mg', 'Analgésico común', 500.0, 200, 20, 'Fármaco');
INSERT OR IGNORE INTO productos (nombre, descripcion, precio, stock, alerta_minima, categoria) VALUES ('Prueba Rápida Malaria', 'Test de diagnóstico', 1000.0, 300, 50, 'Diagnóstico');

