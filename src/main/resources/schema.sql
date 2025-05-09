
-- Eliminar tablas existentes para empezar limpio
DROP TABLE IF EXISTS citas;
DROP TABLE IF EXISTS doctores;
DROP TABLE IF EXISTS consultorios;


-- Tabla de Doctores
CREATE TABLE doctores (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL,
  apellido_paterno VARCHAR(50) NOT NULL,
  apellido_materno VARCHAR(50) NOT NULL,
  especialidad VARCHAR(100) NOT NULL
);

-- Tabla de Consultorios
CREATE TABLE consultorios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  numero_consultorio VARCHAR(10) NOT NULL UNIQUE,
  piso VARCHAR(10) NOT NULL
);

-- Tabla de Citas
CREATE TABLE citas (
   id INT AUTO_INCREMENT PRIMARY KEY,
   consultorio_id INT NOT NULL,
   doctor_id INT NOT NULL,
   fecha_hora TIMESTAMP NOT NULL,
   nombre_paciente VARCHAR(100) NOT NULL,
   estado VARCHAR(20) NOT NULL DEFAULT 'PROGRAMADA',
   FOREIGN KEY (consultorio_id) REFERENCES consultorios(id) ON DELETE CASCADE,
   FOREIGN KEY (doctor_id) REFERENCES doctores(id) ON DELETE CASCADE
);

-- Índices para mejorar el rendimiento de las consultas de validación y búsqueda
CREATE INDEX idx_citas_consultorio_fecha ON citas (consultorio_id, fecha_hora);
CREATE INDEX idx_citas_doctor_fecha ON citas (doctor_id, fecha_hora);
CREATE INDEX idx_citas_paciente_fecha ON citas (nombre_paciente, fecha_hora);
CREATE INDEX idx_citas_fecha ON citas (fecha_hora);
