-- Docker
-- docker cp schema.sql <container_name>:/tmp/schema.sql
-- docker exec -i <container_name> mysql -u <user> -p<password> <database_name> < /tmp/schema.sql

-- Insertar datos de ejemplo en Cliente
INSERT INTO MS_CLI_CLIENTE (nombre, CORREO_ELECTRONICO, cuit, MAXIMO_DESCUBIERTO, maximoObrasActivas)
VALUES 
    ('Juan Pérez', 'juan.perez@example.com', '20-12345678-9', 15000.00, 5),
    ('María López', 'maria.lopez@example.com', '20-98765432-1', 20000.00, 10);

-- Insertar datos de ejemplo en Obra
INSERT INTO MS_CLI_OBRA (direccion, ES_REMODELACION, lat, lng, ID_CLIENTE, presupuesto, estado)
VALUES 
    ('Av. Siempreviva 742', TRUE, -34.603722, -58.381592, 1, 5000.00, 'EN_PROGRESO'),
    ('Calle Falsa 123', FALSE, -34.615803, -58.433298, 2, 10000.00, 'PENDIENTE');

-- Insertar datos de ejemplo en Usuario
INSERT INTO USUARIO (nombre, apellido, dni, CORREO_ELECTRONICO, ID_CLIENTE)
VALUES 
    ('Carlos', 'González', '12345678', 'carlos.gonzalez@example.com', 1),
    ('Ana', 'Martínez', '87654321', 'ana.martinez@example.com', 2);