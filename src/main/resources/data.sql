-- src/main/resources/data.sql

-- Insertar Doctores de ejemplo (3 a 5)
INSERT INTO doctores (nombre, apellido_paterno, apellido_materno, especialidad) VALUES
    ('Juan', 'Perez', 'Garcia', 'Cardiología'),
    ('Maria', 'Lopez', 'Fernandez', 'Neurología'),
    ('Carlos', 'Gonzalez', 'Rodriguez', 'Medicina Interna'),
    ('Ana', 'Martinez', 'Sanchez', 'Medicina Interna');


-- Insertar Consultorios de ejemplo (3 a 5)
INSERT INTO consultorios (numero_consultorio, piso) VALUES
    ('101', '1'),
    ('102', '1'),
    ('201', '2'),
    ('202', '2');


-- Opcional: Insertar algunas citas iniciales si deseas
-- Asegúrate de que los IDs de doctor y consultorio existan.
INSERT INTO citas (consultorio_id, doctor_id, fecha_hora, nombre_paciente, estado) VALUES
    ((SELECT id FROM consultorios WHERE numero_consultorio = '101'), (SELECT id FROM doctores WHERE nombre = 'Juan'), '2025-05-09 10:00:00', 'Paciente X', 'PROGRAMADA'),
    ((SELECT id FROM consultorios WHERE numero_consultorio = '102'), (SELECT id FROM doctores WHERE nombre = 'Maria'), '2025-05-09 10:00:00', 'Paciente Y', 'PROGRAMADA');
